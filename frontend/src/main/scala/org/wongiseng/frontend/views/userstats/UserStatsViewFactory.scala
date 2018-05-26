package org.wongiseng.frontend.views.userstats

import io.udash._
import org.wongiseng.frontend.routing.UserStatsState
import org.wongiseng.shared.model.userstat.{CategoryCount, CategoryStats, UserActivity, UserStats}

class UserStatsViewFactory extends ViewFactory[UserStatsState.type] {

  override def create(): (View, Presenter[UserStatsState.type]) = {
    val model = ModelProperty[UserStatsModel](initModel())
  
    val presenter = new UserStatsPresenter(model)
    val view = new UserStatsView(model, presenter)

    (view, presenter)
  }
  
  def initModel() = {
    val userAgeCategory = CategoryStats("Accounts age",
                                        List(CategoryCount(">= 4 years", 400),
                                             CategoryCount("2-4 years", 300),
                                             CategoryCount("1-2 years", 200),
                                             CategoryCount("< 1 year", 100)
                                        ))

    val followerCategory = CategoryStats("Followers",
                                         List(CategoryCount("< 50", 300),
                                              CategoryCount("50-100", 100),
                                              CategoryCount("100-200", 50),
                                              CategoryCount("200-400", 20),
                                              CategoryCount("400-800", 10),
                                              CategoryCount(">=800", 5)
                                              ))

    val activity = List(UserActivity("wongiseng", 100),
                        UserActivity("hulk", 55),
                        UserActivity("tofa", 22),
                        UserActivity("gmblung", 11)
                        )

    val categoryStats = List(userAgeCategory, followerCategory)
    UserStatsModel(UserStats("#HTI Lanjutkan", 1000, categoryStats, activity))

  }
}
