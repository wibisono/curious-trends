package org.wongiseng.shared.rpc.client

import org.wongiseng.shared.rpc.client.chat.ChatNotificationsRPC
import io.udash.rpc._

@RPC
trait MainClientRPC {
  def chat(): ChatNotificationsRPC
}

object MainClientRPC extends DefaultClientUdashRPCFramework.RPCCompanion[MainClientRPC]