package org.wongiseng.backend.services

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.{Tweet, User}
import org.joda.time.DateTime
import java.util.Date
import scala.collection.mutable
import com.avsystem.commons._

class UserStatService(hashtags : JList[String]) extends UserStats {

  var tweetCount = 0
  val idUsers = mutable.HashMap[Long, User]()
  val userCount = mutable.HashMap[Long, Int]()

  val streamingClient = TwitterStreamingClient()
  streamingClient.filterStatuses(tracks = hashtags.asScala) {
    case tweet: Tweet => {
      tweetCount += 1
      println(s"@${tweet.user.map(_.screen_name).orNull} : ${tweet.text}")
      tweet.user.map(user => {
        val curcount = userCount.getOrElseUpdate(user.id, 0)
        userCount.put(user.id, curcount + 1)
        idUsers.put(user.id, user)
      })

      if (idUsers.size % 10 == 0) {
        computeStatistic(idUsers.values.toSet, tweetCount)

        println("Top 20 Users: ")
        userCount.toSeq.sortBy(-_._2).take(20).foreach {
          case (userId, count) => println(idUsers(userId).screen_name, count)
        }
      }
    }
  }
}

trait  UserStats {

  def percent(cnt: Int, tot: Int) = cnt * 100.0 / tot 

  def followers_limit(low : Int = Int.MinValue, high : Int = Int.MaxValue)(u : User) = u.followers_count < high && u.followers_count >= low
  def created_limit(after: Date = DateTime.now().minusYears(100).toDate, 
                    before: Date = DateTime.now().plusYears(100).toDate)(u : User)   = u.created_at.after(after) && u.created_at.before(before)

  def computeStatistic(users: Set[User], tweetCount: Int) = {
    val totalUser = users.size 
    
    val less50      = users.count(followers_limit(high = 50))
    val from50_100  = users.count(followers_limit(50, 100))
    val from100_200 = users.count(followers_limit(100,200))
    val from200_400 = users.count(followers_limit(200,400))
    val from400_800 = users.count(followers_limit(400,800))
    val over800     = users.count(followers_limit(low=800))

    println( "================================================================================")
    println( s"=== Analyzing follower and account age of tweeting #HTILanjutkan Perjuangan ===")
    println( "================================================================================")
    println( s"Total user found so far : ${users.size} tweet counts : $tweetCount")
    println( "================================================================================")

    println(s"Follower < 50:           ${percent(less50, totalUser)}%")
    println(s"50  <= Follower <= 100:  ${percent(from50_100, totalUser)}%")
    println(s"100  <= Follower <= 200: ${percent(from100_200, totalUser)}%")
    println(s"200  <= Follower <= 400: ${percent(from200_400, totalUser)}%")
    println(s"400  <= Follower <= 800: ${percent(from400_800, totalUser)}%")
    println(s"Follower >= 800:         ${percent(over800, totalUser)}")

    val age3m = DateTime.now().minusMonths(3).toDate
    val age6m = DateTime.now().minusMonths(6).toDate
    val age1y = DateTime.now().minusYears(1).toDate
    val age2y = DateTime.now().minusYears(2).toDate
    val age4y = DateTime.now().minusYears(4).toDate

    val cntAge3m     = users.count(created_limit(after=age3m))
    val cntAge3to6m  = users.count(created_limit(age3m, age6m))
    val cntAge6mto1y = users.count(created_limit(age6m, age1y))
    val cntAge1yto2y = users.count(created_limit(age1y, age2y))
    val cntAge2yto4y = users.count(created_limit(age2y, age4y))
    val cntOlder4y   = users.count(created_limit(before=age4y))

    println( "================================================================================")
    println( "======= Account age                                                   ==========")
    println( "================================================================================")

    println(s"  < 3  months ${percent(cntAge3m, totalUser)}%")
    println(s"3 - 6  months ${percent(cntAge3to6m, totalUser)}%")
    println(s"6 - 12 months ${percent(cntAge6mto1y, totalUser)}%")
    println(s"1 - 2  years  ${percent(cntAge1yto2y, totalUser)}%")
    println(s"2 - 4  years  ${percent(cntAge2yto4y, totalUser)}%")
    println(s">   4  years  ${percent(cntOlder4y, totalUser)}%")
    println(
      "================================================================================")

  }
}
