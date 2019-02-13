package utils

import models.GuildHistorySnapshot
import net.dv8tion.jda.core.JDA
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

object ChartMaker {

    fun channelStatsChart(
        chartName: String,
        xName: String,
        yName: String,
        history: GuildHistorySnapshot.ChannelHistorySnapShot
    ): InputStream {

        val chart = XYChartBuilder().width(600).height(600).title(chartName).xAxisTitle(xName).yAxisTitle(yName).build()
        chart.styler.isLegendVisible = false

        chart.apply {
            addSeries(chartName, history.xData, history.yData)
        }
        return ByteArrayInputStream(BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))
    }


    fun guildTopChannelCharts(
        data: GuildHistorySnapshot,
        xName: String = "Channel",
        yName: String = "Messages",
        title: String? = null
    ): InputStream {
        val chart =
            XYChartBuilder().width(600)
                .height(600)
                .title(title ?: data.guild.name ?: "Historical Data")
                .xAxisTitle(xName)
                .yAxisTitle(yName)
                .build()

        chart.styler.apply {
            legendPosition = Styler.LegendPosition.OutsideS
            defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
            chartTitlePadding = 20
            timezone = TimeZone.getDefault()
            yAxisMax = data.maxYSize + 5.0
            axisTitlePadding = 5
        }

        for (channelData in data.history.reversed()) {
            val channel = data.guild.getTextChannelById(channelData.channel)?.name ?: channelData.channel.toString()
            chart.addSeries(channel, channelData.xData, channelData.yData)
        }
        return ByteArrayInputStream(BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))

    }

    fun customChannelCharts(
        data: List<GuildHistorySnapshot.ChannelHistorySnapShot>,
        jda: JDA,
        xName: String = "Channel",
        yName: String = "Messages",
        title: String? = null
    ): InputStream {
        val chart =
            XYChartBuilder().width(600)
                .height(600)
                .title(title ?: "Historical Data")
                .xAxisTitle(xName)
                .yAxisTitle(yName)
                .build()

        chart.styler.apply {
            legendPosition = Styler.LegendPosition.OutsideE
            defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
            chartTitlePadding = 20
            timezone = TimeZone.getDefault()
            axisTitlePadding = 5
            yAxisMax = 10.0
            isYAxisLogarithmic = true
        }



        for (channelData in data) {
            val channel = jda.getTextChannelById(channelData.channel)?.name ?: channelData.channel.toString()
            chart.addSeries(channel, channelData.xData, channelData.yData)
        }
        return ByteArrayInputStream(BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG))

    }


}