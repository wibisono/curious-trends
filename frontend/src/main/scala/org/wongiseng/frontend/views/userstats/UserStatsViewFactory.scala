package org.wongiseng.frontend.views.userstats

import io.udash.{Presenter, _}
import io.udash.properties.model.ModelProperty
import org.wongiseng.frontend.routing.UserStatsState
import org.wongiseng.frontend.services.rpc.NotificationsCenter
import org.wongiseng.shared.model.userstat.UserStats
import org.wongiseng.shared.rpc.server.trends.TrendsRPC

class UserStatsViewFactory(notificationCenter: NotificationsCenter, trendsRPC: TrendsRPC, hashtag: String) extends
  ViewFactory[UserStatsState] {

  override def create(): (View, Presenter[UserStatsState]) = {
    val model = ModelProperty[UserStatsModel](UserStatsModel(UserStats("",0,List(),List())))
  
    val presenter = new UserStatsPresenter(model, notificationCenter, trendsRPC)
    val view = new UserStatsView(model, presenter)

    (view, presenter)
  }

}
