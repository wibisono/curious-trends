package org.wongiseng.backend.services

import java.util.Date

import com.avsystem.commons._
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{Tweet, User}
import io.udash.rpc.AllClients
import org.joda.time.DateTime
import org.wongiseng.shared.model.userstat.{CategoryCount, CategoryStats, UserActivity, UserStats}

import scala.collection.mutable

class TrendsService(hashtags : JList[String], rpcClientsService: RpcClientsService) extends UserCategories {


  var tweetCount = 0
  val idUsers = mutable.HashMap[Long, User]()
  val userCount = mutable.HashMap[Long, Int]()

  private val hashTags= hashtags.asScala.toList

  val streamingClient = TwitterStreamingClient()
  streamingClient.filterStatuses(tracks = hashTags) {
      case tweet: Tweet => {

        tweetCount += 1
        tweet.user.map(user => {
          val curCount = userCount.getOrElseUpdate(user.id, 0)
          userCount.put(user.id, curCount + 1)
          idUsers.put(user.id, user)
        })

        val ages = accountAges(idUsers.values.toSet)
        val followers = followerCounts(idUsers.values.toSet)
        val activities = userCount.toList.sortBy(-_._2).take(20).map {
         case (userId, count) => UserActivity(idUsers(userId).screen_name, count)
        }

        val userStats = UserStats(
          hashTags.mkString(" "), tweetCount,
          List(ages, followers),
          activities
        )

        rpcClientsService.sendToClient(AllClients)
          .trends().newUserStats(userStats)

      }
    }

}

trait UserCategories {



  def followers_limit(low : Int = Int.MinValue, high : Int = Int.MaxValue)(u : User) = u.followers_count < high && u.followers_count >= low
  def created_limit(after: Date = DateTime.now().minusYears(100).toDate,
                    before: Date = DateTime.now().plusYears(100).toDate)(u : User)   = u.created_at.after(after) && u.created_at.before(before)

  def followerCounts(users: Set[User]) = {
    val less50 = CategoryCount("<= 50", users.count(followers_limit(high = 50)))
    val from50_100 = CategoryCount("> 50 and <= 100", users.count(followers_limit(50, 100)))
    val from100_200 = CategoryCount("> 100 and <= 200", users.count(followers_limit(100, 200)))
    val from200_400 = CategoryCount("> 200 and <= 400", users.count(followers_limit(200, 400)))
    val from400_800 = CategoryCount("> 400 and <= 800", users.count(followers_limit(400, 800)))
    val over800 = CategoryCount("> 800 ", users.count(followers_limit(low = 800)))

    CategoryStats("Follower counts", List(less50, from50_100, from100_200, from200_400, from400_800, over800))
  }

  def accountAges(users: Set[User]) = {
    val age3m = DateTime.now().minusMonths(3).toDate
    val age6m = DateTime.now().minusMonths(6).toDate
    val age1y = DateTime.now().minusYears(1).toDate
    val age2y = DateTime.now().minusYears(2).toDate
    val age4y = DateTime.now().minusYears(4).toDate

    val cntAge3m     = CategoryCount("<= 3m",users.count(created_limit(after=age3m)))
    val cntAge3to6m  = CategoryCount("3 months util 6 months",users.count(created_limit(age3m, age6m)))
    val cntAge6mto1y = CategoryCount("6 months util 12 months",users.count(created_limit(age6m, age1y)))
    val cntAge1yto2y = CategoryCount("12 months util 24 months",users.count(created_limit(age1y, age2y)))
    val cntAge2yto4y = CategoryCount("24 months util 36 months",users.count(created_limit(age2y, age4y)))
    val cntOlder4y   = CategoryCount("> 48 months ",users.count(created_limit(before=age4y)))

    CategoryStats("Account age", List(cntAge3m, cntAge3to6m, cntAge6mto1y, cntAge1yto2y, cntAge2yto4y, cntOlder4y))
  }
}
