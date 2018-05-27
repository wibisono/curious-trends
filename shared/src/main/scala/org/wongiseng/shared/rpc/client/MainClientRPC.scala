package org.wongiseng.shared.rpc.client

import org.wongiseng.shared.rpc.client.chat.ChatNotificationsRPC
import io.udash.rpc._
import org.wongiseng.shared.rpc.client.trends.TrendsNotificationRPC

@RPC
trait MainClientRPC {
  def chat(): ChatNotificationsRPC
  def trends(): TrendsNotificationRPC
}

object MainClientRPC extends DefaultClientUdashRPCFramework.RPCCompanion[MainClientRPC]