package org.wongiseng.frontend.services.rpc

import org.wongiseng.shared.rpc.client.MainClientRPC
import org.wongiseng.shared.rpc.client.chat.ChatNotificationsRPC

class RPCService(notificationsCenter: NotificationsCenter) extends MainClientRPC {
  override val chat: ChatNotificationsRPC =
    new ChatService(notificationsCenter.msgListeners, notificationsCenter.connectionsListeners)
}