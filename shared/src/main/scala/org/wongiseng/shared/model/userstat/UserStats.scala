package org.wongiseng.shared.model.userstat

import com.avsystem.commons.serialization.HasGenCodec

case class CategoryCount(label: String, count: Int)
object CategoryCount extends HasGenCodec[CategoryCount]

case class CategoryStats(category: String, counts: List[CategoryCount])
object CategoryStats extends HasGenCodec[CategoryStats]

case class UserActivity(name : String, count : Int)
object UserActivity extends HasGenCodec[UserActivity]

case class UserStats(hashTag : String, totalTweets: Int, stats: List[CategoryStats], activity: List[UserActivity])
object UserStats extends HasGenCodec[UserStats]