package org.wongiseng.backend.server

import io.udash.rest.server.{DefaultExposesREST, DefaultRestServlet}
import org.wongiseng.backend.rpc.ExposedRpcInterfaces
import org.wongiseng.backend.services.DomainServices
import org.wongiseng.shared.model.SharedExceptions
import org.wongiseng.shared.rpc.server.MainServerRPC
import io.udash.rpc._
import io.udash.rpc.utils.DefaultAtmosphereFramework
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}
import org.wongiseng.backend.rest.RestImplementation
import org.wongiseng.shared.rest.MainServerREST

import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationServer(val port: Int, resourceBase: String, domainServices: DomainServices) {
  private val server = new Server(port)
  private val contextHandler = new ServletContextHandler
  private val appHolder = createAppHolder()
  private val atmosphereHolder = createAtmosphereHolder()
  private val restHolder = new ServletHolder(
    new DefaultRestServlet(new DefaultExposesREST[MainServerREST](new RestImplementation)))
  restHolder.setAsyncSupported(true)

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.getSessionHandler.addEventListener(new org.atmosphere.cpr.SessionSupport())
  contextHandler.addServlet(atmosphereHolder, "/atm/*")
  contextHandler.addServlet(restHolder, "/rest/*")
  contextHandler.addServlet(appHolder, "/*")
  server.setHandler(contextHandler)

  def start(): Unit = server.start()
  def stop(): Unit = server.stop()

  private def createAtmosphereHolder() = {
    val config = new DefaultAtmosphereServiceConfig((clientId) =>
      // interfaces are cached per user connection
      new DefaultExposesServerRPC[MainServerRPC](
        new ExposedRpcInterfaces()(domainServices, clientId)
      )
    )

    // notify ClientsService about new or closed connections
    config.onNewConnection { case id => domainServices.rpcClientsService.registerConnection(id) }
    config.onClosedConnection { case id => domainServices.rpcClientsService.unregisterConnection(id) }

    val framework = new DefaultAtmosphereFramework(
      config,
      // this is registry with custom exceptions support
      exceptionsRegistry = new SharedExceptions
    )

    val atmosphereHolder = new ServletHolder(new RpcServlet(framework))
    atmosphereHolder.setAsyncSupported(true)
    atmosphereHolder
  }

  private def createAppHolder() = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }
}
