package org.wongiseng.frontend.views.userstats

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.panel.{PanelStyle, UdashPanel}
import io.udash.bootstrap.table.UdashTable
import io.udash.bootstrap.utils.UdashPageHeader
import io.udash.css.CssView
import io.udash.wrappers.highcharts.HighchartsUtils._
import io.udash.wrappers.jquery.jQ
import org.wongiseng.frontend.views.userstats.charts.CategoryPieChart
import org.wongiseng.shared.model.userstat.CategoryStats
import scalatags.JsDom.all._

class UserStatsView(model: ModelProperty[UserStatsModel], presenter: UserStatsPresenter)
  extends ContainerView with CssView {

  override def getTemplate: Modifier = {

    def categoryChart(stat: CategoryStats) = {
      val c = div().render
      jQ(c).highcharts(CategoryPieChart.config(stat))

      div(BootstrapStyles.Grid.colXs6, BootstrapStyles.Well.well)(
        UdashPanel(PanelStyle.Success)(
          UdashPanel.heading(stat.category),
          UdashPanel.body(c)
        ).render
      ).render
    }

    val hashTag = model.subProp(_.userStats).transform(_.hashTag)
    val totalTweets = model.subProp(_.userStats).transform(_.totalTweets)
    val categories = model.subProp(_.userStats).transformToSeq(_.stats)
    val activities = model.subProp(_.userStats).transformToSeq(_.activity)

    val striped = Property(true)
    val bordered = Property(true)
    val hover = Property(true)
    val condensed = Property(false)

    val activityTable = UdashTable(striped, bordered, hover, condensed)(activities)(
      headerFactory = Some(() => tr(th(b("Username")), th(b("Tweet count"))).render),
      rowFactory = (el) => tr(
        td(produce(el)(v => a(href:=s"https://twitter.com/${v.name}")(v.name).render)),
        td(produce(el)(v => i(v.count).render)),
      ).render
    )

    div(cls := "bootstrap",
        UdashPageHeader(
          h1("Tracked keywords #", bind(hashTag), small("      | Total tweets:", bind(totalTweets)))).render,
        div(BootstrapStyles.row)(
          repeat(categories)(e => categoryChart(e.get)),
          div(BootstrapStyles.Grid.colXs12, BootstrapStyles.Well.well)(
            UdashPanel(PanelStyle.Success)(
              UdashPanel.heading(s"User activity"),
              activityTable.render
            ).render
          )
        ),
        childViewContainer // child view container provided by ContainerView
    )
  }
}
