package org.wongiseng.frontend.views.userstats

import io.udash._

import org.wongiseng.frontend.routing.UserStatsState
import org.wongiseng.frontend.services.rpc.NotificationsCenter

class UserStatsPresenter(model: ModelProperty[UserStatsModel],
                         notificationCenter : NotificationsCenter) extends Presenter[UserStatsState.type] {

  val statsCallback = notificationCenter.onNewUserStats { case userStats =>
      model.subProp(_.userStats).set(userStats)
  }
  override def handleState(state: UserStatsState.type) = {

  }

  override def onClose(): Unit = {
    statsCallback.cancel()
  }

}