package org.wongiseng.backend.rpc.secure

import org.wongiseng.backend.rpc.secure.chat.ChatEndpoint
import org.wongiseng.backend.services.DomainServices
import org.wongiseng.shared.model.auth.UserContext
import org.wongiseng.shared.rpc.server.secure.SecureRPC
import org.wongiseng.shared.rpc.server.secure.chat.ChatRPC

class SecureEndpoint(implicit domainServices: DomainServices, ctx: UserContext) extends SecureRPC {
  import domainServices._

  lazy val chatEndpoint = new ChatEndpoint

  override def chat(): ChatRPC = chatEndpoint
}
