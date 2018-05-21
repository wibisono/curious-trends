package org.wongiseng.backend.rpc

import org.wongiseng.backend.rpc.auth.AuthEndpoint
import org.wongiseng.backend.rpc.i18n.TranslationsEndpoint
import org.wongiseng.backend.rpc.secure.SecureEndpoint
import org.wongiseng.backend.services.DomainServices
import org.wongiseng.shared.model.auth.{UserContext, UserToken}
import org.wongiseng.shared.model.SharedExceptions
import org.wongiseng.shared.rpc.server.MainServerRPC
import org.wongiseng.shared.rpc.server.open.AuthRPC
import org.wongiseng.shared.rpc.server.secure.SecureRPC
import io.udash.i18n.RemoteTranslationRPC
import io.udash.rpc._

class ExposedRpcInterfaces(implicit domainServices: DomainServices, clientId: ClientId) extends MainServerRPC {
  // required domain services are implicitly passed to the endpoints
  import domainServices._

  private lazy val authEndpoint: AuthRPC = new AuthEndpoint

  // it caches SecureEndpoint for a single UserToken (UserToken change is not an expected behaviour)
  private var secureEndpointCache: Option[(UserToken, SecureEndpoint)] = None

  private def secureEndpoint(implicit ctx: UserContext): SecureRPC = {
    secureEndpointCache match {
      case Some((token, endpoint)) if token == ctx.token =>
        endpoint
      case None =>
        val endpoint = new SecureEndpoint
        secureEndpointCache = Some((ctx.token, endpoint))
        endpoint
    }
  }

  override def auth(): AuthRPC = authEndpoint

  override def secure(token: UserToken): SecureRPC = {
    authService
      .findUserCtx(token)
      .map(ctx => secureEndpoint(ctx))
      .getOrElse(throw SharedExceptions.UnauthorizedException())
  }

  override def translations(): RemoteTranslationRPC = TranslationsEndpoint
}