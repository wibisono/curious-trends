package org.wongiseng.shared.rpc.server

import org.wongiseng.shared.model.auth.UserToken
import org.wongiseng.shared.rpc.server.open.AuthRPC
import org.wongiseng.shared.rpc.server.secure.SecureRPC
import io.udash.i18n._
import io.udash.rpc._
import org.wongiseng.shared.rpc.server.trends.TrendsRPC

@RPC
trait MainServerRPC {
  /** Returns an RPC for authentication. */
  def auth(): AuthRPC

  /** Verifies provided UserToken and returns a [[org.wongiseng.shared.rpc.server.secure.SecureRPC]] if the token is valid. */
  def secure(token: UserToken): SecureRPC

  /** Returns an RPC serving translations from the server resources. */
  def translations(): RemoteTranslationRPC

  /** Getting trends RPC */
  def trends(): TrendsRPC
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]