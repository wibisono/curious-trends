package org.wongiseng.frontend.views.userstats

import io.udash.{Presenter, _}
import io.udash.properties.model.ModelProperty
import org.wongiseng.frontend.routing.UserStatsState
import org.wongiseng.frontend.services.rpc.NotificationsCenter
import org.wongiseng.shared.model.userstat.UserStats

class UserStatsViewFactory(notificationCenter: NotificationsCenter) extends ViewFactory[UserStatsState.type] {

  override def create(): (View, Presenter[UserStatsState.type]) = {
    val model = ModelProperty[UserStatsModel](UserStatsModel(UserStats("",0,List(),List())))
  
    val presenter = new UserStatsPresenter(model, notificationCenter)
    val view = new UserStatsView(model, presenter)

    (view, presenter)
  }

}
