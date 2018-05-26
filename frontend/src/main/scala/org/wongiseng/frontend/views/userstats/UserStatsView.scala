package org.wongiseng.frontend.views.userstats

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.bootstrap.utils.UdashListGroup
import io.udash.css.CssView
import io.udash.wrappers.highcharts.HighchartsUtils._
import io.udash.wrappers.jquery.jQ
import org.scalajs.dom.Element
import org.wongiseng.frontend.views.userstats.charts.CategoryPieChart
import org.wongiseng.shared.model.userstat.{CategoryStats, UserStats}
import scalatags.JsDom.all._

class UserStatsView(model: ModelProperty[UserStatsModel], presenter: UserStatsPresenter)
  extends ContainerView with CssView {

  private def chartContainer(): Element =
    div().render

  override def getTemplate: Modifier = {
    val userStat: UserStats = model.subProp(_.userStats).get

    def categoryChart(stat: CategoryStats) = {
      val c = chartContainer()
      jQ(c).highcharts(CategoryPieChart.config(stat))

      div(BootstrapStyles.Grid.colXs6, BootstrapStyles.Well.well)(
        UdashPanel(PanelStyle.Success)(
          UdashPanel.heading(stat.category),
          UdashPanel.body(c)
        ).render
      ).render
    }

    div(cls := "bootstrap",
        div(BootstrapStyles.row)(
          div(BootstrapStyles.Grid.colXs12, BootstrapStyles.Well.well)(
            h3(s"User statistics for: ${userStat.hashTag} sampled from #${userStat.totalTweets} tweets")
          ),
          repeat(model.subProp(_.userStats).transformToSeq(_.stats))(e=>categoryChart(e.get)),
          div(BootstrapStyles.Grid.colXs12, BootstrapStyles.Well.well)(
            UdashPanel(PanelStyle.Success)(
              UdashPanel.heading(s"User activity"),
              UdashListGroup(model.subProp(_.userStats).transformToSeq(_.activity)){ act =>
                li(bind(act)).render
              }.render
            ).render
          )
        ),
        childViewContainer // child view container provided by ContainerView
    )
  }
}
