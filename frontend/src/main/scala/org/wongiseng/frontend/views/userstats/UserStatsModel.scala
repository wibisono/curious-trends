package org.wongiseng.frontend.views.userstats

import io.udash.properties.HasModelPropertyCreator
import org.wongiseng.shared.model.userstat.UserStats

case class UserStatsModel( userStats : UserStats)
object UserStatsModel extends HasModelPropertyCreator[UserStatsModel]