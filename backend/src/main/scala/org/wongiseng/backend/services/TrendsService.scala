package org.wongiseng.backend.services

import java.util.Date

import com.avsystem.commons._
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{Tweet, User}
import io.udash.logging.CrossLogging
import io.udash.rpc.ClientId
import org.joda.time.DateTime
import org.wongiseng.shared.model.userstat.{
  CategoryCount,
  CategoryStats,
  UserActivity,
  UserStats
}
import org.wongiseng.shared.rest.{Graph, Link, Node}

import scala.collection.mutable

class TrendsService(rpcClientsService: RpcClientsService)
    extends UserTrends
    with CrossLogging {

  import singletonData._

  val streamingClient = TwitterStreamingClient()

  def subscribeHashtag(hashtag: String)(
      implicit clientId: ClientId): Future[Unit] = {
    logger.info("Subscribing hashtag: " + hashtag + " for client " + clientId)
    observeHashtag(hashtag, clientId)
    Future.unit
  }

  def follower_group(user : User) =
    if(user.followers_count < 50) 0
    else if(user.followers_count < 100) 1
    else if(user.followers_count < 200) 2
    else if(user.followers_count < 400) 3
    else if(user.followers_count < 800) 4
    else if(user.followers_count < 1600) 5
    else if(user.followers_count < 3200) 6
    else 7

  private def observeHashtag(hashtag: String, clientId: ClientId) = {
    hashTags = hashTags + hashtag

    logger.info("Current hashtag set: " + hashTags.mkString(", "))

    val seqHashtag = hashTags.toList

    streamingClient.filterStatuses(tracks = seqHashtag) {
      case tweet: Tweet => {

        tweetCount += 1

        tweet.user.map(user => {
          val curCount = userCount.getOrElseUpdate(user.id, 0)
          if (curCount == 0) {
            logger.info("Adding to graph" + user.screen_name)
            nodes += Node(user.screen_name, user.name, follower_group(user))
          }
          userCount.put(user.id, curCount + 1)
          idUsers.put(user.id, user)
        })

        tweet.retweeted_status.map { originalTweet =>
          val retweetCount = originalTweet.retweet_count.toInt
          for {
            origUser <- originalTweet.user
            curUser <- tweet.user
          } yield {
            if (!idUsers.contains(origUser.id)) {
              logger.info("Adding orig to graph " + origUser.screen_name)
              nodes += Node(origUser.screen_name, origUser.screen_name, follower_group(origUser))
            }
            logger.info(
              "Updating retweet  graph " + curUser.screen_name + " -> " + origUser.screen_name)
            links += Link(curUser.screen_name,
                          origUser.screen_name,
                          retweetCount)
          }
        }

        logger.info("Updating client : " + clientId)
        if (tweetCount % 10 == 0) {
          if (rpcClientsService.activeClients.size > 0)
            rpcClientsService
              .sendToClient(clientId)
              .trends()
              .newUserStats(currentStats())
        }
      }
    }

  }
}

trait UserTrends extends InMemoryStats {
  import singletonData._

  def currentStats() = {

    val ages = accountAges(idUsers.values.toSet)
    val followers = followerCounts(idUsers.values.toSet)
    val activities = userCount.toList.sortBy(-_._2).take(20).map {
      case (userId, count) => UserActivity(idUsers(userId).screen_name, count)
    }
    UserStats(
      hashTags.mkString(" "),
      tweetCount,
      List(ages, followers),
      activities
    )
  }

  def followers_limit(low: Int = Int.MinValue, high: Int = Int.MaxValue)(
      u: User) = u.followers_count < high && u.followers_count >= low
  def created_limit(
      after: Date = DateTime.now().minusYears(100).toDate,
      before: Date = DateTime.now().plusYears(100).toDate)(u: User) =
    u.created_at.after(after) && u.created_at.before(before)

  def followerCounts(users: Set[User]) = {
    val less50 = CategoryCount("<= 50", users.count(followers_limit(high = 50)))
    val from50_100 =
      CategoryCount("> 50 and <= 100", users.count(followers_limit(50, 100)))
    val from100_200 =
      CategoryCount("> 100 and <= 200", users.count(followers_limit(100, 200)))
    val from200_400 =
      CategoryCount("> 200 and <= 400", users.count(followers_limit(200, 400)))
    val from400_800 =
      CategoryCount("> 400 and <= 800", users.count(followers_limit(400, 800)))
    val over800 =
      CategoryCount("> 800 ", users.count(followers_limit(low = 800)))

    CategoryStats(
      "Follower counts",
      List(less50, from50_100, from100_200, from200_400, from400_800, over800))
  }

  def accountAges(users: Set[User]) = {
    val age3m = DateTime.now().minusMonths(3).toDate
    val age6m = DateTime.now().minusMonths(6).toDate
    val age1y = DateTime.now().minusYears(1).toDate
    val age2y = DateTime.now().minusYears(2).toDate
    val age4y = DateTime.now().minusYears(4).toDate

    val cntAge3m =
      CategoryCount("<= 3m", users.count(created_limit(after = age3m)))
    val cntAge3to6m = CategoryCount("3 months util 6 months",
                                    users.count(created_limit(age6m, age3m)))
    val cntAge6mto1y = CategoryCount("6 months util 12 months",
                                     users.count(created_limit(age1y, age6m)))
    val cntAge1yto2y = CategoryCount("12 months util 24 months",
                                     users.count(created_limit(age2y, age1y)))
    val cntAge2yto4y = CategoryCount("24 months util 36 months",
                                     users.count(created_limit(age4y, age2y)))
    val cntOlder4y =
      CategoryCount("> 48 months ", users.count(created_limit(before = age4y)))

    CategoryStats("Account age",
                  List(cntAge3m,
                       cntAge3to6m,
                       cntAge6mto1y,
                       cntAge1yto2y,
                       cntAge2yto4y,
                       cntOlder4y))
  }
}

trait InMemoryStats extends CrossLogging {
  import singletonData._

  def retweetGraph() = {
    logger.info("Returning retweet with " + nodes.toString + " " + links)
    Graph(nodes.toList, links.toList)
  }
}

object singletonData {
  var hashTags = Set[String]()
  var tweetCount = 0
  val idUsers = mutable.HashMap[Long, User]()
  val userCount = mutable.HashMap[Long, Int]()
  val nodes = mutable.Set[Node]()
  val links = mutable.Set[Link]()
}
