package org.wongiseng.backend.rest

import io.udash.logging.CrossLogging
import org.wongiseng.backend.services.UserTrends
import org.wongiseng.shared.rest._

import scala.concurrent.Future

class RestImplementation extends MainServerREST with UserTrends with CrossLogging {
  override def simple(): SimpleServerREST = () => {
    logger.info(retweetGraph().toString)
    Future.successful(retweetGraph())
  }
}
