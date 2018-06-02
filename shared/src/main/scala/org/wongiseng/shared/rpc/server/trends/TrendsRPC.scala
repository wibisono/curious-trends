package org.wongiseng.shared.rpc.server.trends

import io.udash.rpc.{DefaultServerUdashRPCFramework, RPC}

import scala.concurrent.Future

@RPC
trait TrendsRPC {
    def subscribeHashtag(hashtag : String) : Future[Unit]
}

object TrendsRPC extends DefaultServerUdashRPCFramework.RPCCompanion[TrendsRPC]
