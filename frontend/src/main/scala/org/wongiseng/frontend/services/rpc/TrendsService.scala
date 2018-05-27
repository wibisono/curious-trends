package org.wongiseng.frontend.services.rpc

import io.udash.utils.CallbacksHandler
import org.wongiseng.shared.model.userstat.UserStats
import org.wongiseng.shared.rpc.client.trends.TrendsNotificationRPC

class TrendsService( userStatsListener: CallbacksHandler[UserStats],
                     connectionsListeners: CallbacksHandler[Int])
      extends TrendsNotificationRPC {
  
  override def newUserStats(stats: UserStats): Unit =
    userStatsListener.fire(stats)

  override def connectionsCountUpdate(count: Int): Unit =
    connectionsListeners.fire(count)
}

