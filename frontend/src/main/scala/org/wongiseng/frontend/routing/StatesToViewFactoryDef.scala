package org.wongiseng.frontend.routing

import org.wongiseng.frontend.ApplicationContext
import org.wongiseng.frontend.views.RootViewFactory
import org.wongiseng.frontend.views.chat.ChatViewFactory
import org.wongiseng.frontend.views.login.LoginPageViewFactory
import io.udash._
import org.wongiseng.frontend.views.userstats.UserStatsViewFactory

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] =
    state match {
      case RootState => new RootViewFactory(
        ApplicationContext.translationsService
      )
      case LoginPageState => new LoginPageViewFactory(
        ApplicationContext.userService, ApplicationContext.application, ApplicationContext.translationsService
      )
      case ChatState => new ChatViewFactory(
        ApplicationContext.userService, ApplicationContext.translationsService, ApplicationContext.notificationsCenter
      )
      case UserStatsState(hashtag) =>
        new UserStatsViewFactory(
          ApplicationContext.notificationsCenter,
          ApplicationContext.serverRpc.trends(),
          hashtag
        )
    }
}