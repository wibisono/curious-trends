package org.wongiseng.shared.rpc.client.trends

import io.udash.rpc._
import org.wongiseng.shared.model.userstat.UserStats

@RPC
trait TrendsNotificationRPC {
  /** Notification about user statistics on server side. */
  def newUserStats(msg: UserStats): Unit

  /** Notification about authenticated connections count change. */
  def connectionsCountUpdate(count: Int): Unit
}


