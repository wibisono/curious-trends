package org.wongiseng.backend.rpc.trends

import io.udash.rpc.ClientId
import org.wongiseng.backend.services.TrendsService
import org.wongiseng.shared.rpc.server.trends.TrendsRPC

import scala.concurrent.Future

class TrendsEndpoint(implicit trendsService : TrendsService, clientId: ClientId)
extends TrendsRPC {
  override def subscribeHashtag(hashtag: String): Future[Unit] = trendsService.subscribeHashtag(hashtag)
}
