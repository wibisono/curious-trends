package org.wongiseng.shared.rest

import io.udash.rest._

import scala.concurrent.Future

@REST
trait MainServerREST {
  def simple(): SimpleServerREST
}

@REST
trait SimpleServerREST {
  @GET def graph(): Future[Graph]
}

