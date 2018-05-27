package org.wongiseng.frontend.services.rpc

import org.wongiseng.shared.rpc.client.MainClientRPC
import org.wongiseng.shared.rpc.client.chat.ChatNotificationsRPC
import org.wongiseng.shared.rpc.client.trends.TrendsNotificationRPC

class RPCService(notificationsCenter: NotificationsCenter) extends MainClientRPC {
  override val chat: ChatNotificationsRPC =
    new ChatService(notificationsCenter.msgListeners, notificationsCenter.connectionsListeners)

  override val trends: TrendsNotificationRPC =
    new TrendsService(notificationsCenter.statsListeners, notificationsCenter.connectionsListeners)

}