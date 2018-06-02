package org.wongiseng.frontend.views.userstats.charts

import io.udash.wrappers.highcharts.config.HighchartsConfig
import io.udash.wrappers.highcharts.config.legend.Legend
import io.udash.wrappers.highcharts.config.series.pie.{SeriesPie, SeriesPieData}
import io.udash.wrappers.highcharts.config.title.Title
import io.udash.wrappers.highcharts.config.utils.Animation
import org.wongiseng.shared.model.userstat.CategoryStats

object CategoryPieChart {
  def config(stat: CategoryStats) = HighchartsConfig(
    title = Title(
      text = s"${stat.category}"
    ),
    legend = Legend(enabled = false),

    series = Seq(
      SeriesPie(
        animation = Animation.Disabled,
        name = s"${stat.category}",
        data = stat.counts.map(c => SeriesPieData(name = c.label, y = c.count*1.0))
      )
    )
  )
}
