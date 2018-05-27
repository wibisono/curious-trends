package org.wongiseng.frontend.services.rpc

import org.wongiseng.shared.model.chat.ChatMessage
import io.udash.utils.{CallbacksHandler, Registration}
import org.wongiseng.shared.model.userstat.UserStats

/** Provides notifications about new messages and connections status. */
class NotificationsCenter {
  private[rpc] val msgListeners: CallbacksHandler[ChatMessage] = new CallbacksHandler[ChatMessage]
  private[rpc] val statsListeners: CallbacksHandler[UserStats] = new CallbacksHandler[UserStats]
  private[rpc] val connectionsListeners: CallbacksHandler[Int] = new CallbacksHandler[Int]

  def onNewMsg(callback: msgListeners.CallbackType): Registration =
    msgListeners.register(callback)

  def onNewUserStats(callback : statsListeners.CallbackType) : Registration =
    statsListeners.register(callback)

  def onConnectionsCountChange(callback: connectionsListeners.CallbackType): Registration =
    connectionsListeners.register(callback)
}
