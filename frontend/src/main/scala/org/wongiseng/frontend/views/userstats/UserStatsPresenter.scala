package org.wongiseng.frontend.views.userstats

import io.udash._
import org.wongiseng.frontend.routing.UserStatsState
import org.wongiseng.frontend.services.rpc.NotificationsCenter
import org.wongiseng.shared.rpc.server.trends.TrendsRPC

class UserStatsPresenter(model: ModelProperty[UserStatsModel],
                         notificationCenter : NotificationsCenter,
                         trendsRPC : TrendsRPC
                        ) extends Presenter[UserStatsState] {

  val statsCallback = notificationCenter.onNewUserStats { case userStats =>
      model.subProp(_.userStats).set(userStats)
  }
  override def handleState(state: UserStatsState) = {
      trendsRPC.subscribeHashtag(state.hashtag)
  }

  override def onClose(): Unit = {
    statsCallback.cancel()
  }

}