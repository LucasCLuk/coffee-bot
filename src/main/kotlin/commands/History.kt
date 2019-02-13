package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.commons.utils.FinderUtil
import utils.*

class History : Command() {


    init {
        name = "history"
        help = "Displays Historical stats for a specific target. Can be either a Person/Channel/Category/Guild"
        arguments = "target"
        guildOnly = true
        children = arrayOf(
            Day(),
            Week(),
            HistoricalComparision()
        )
    }

    override fun execute(event: CommandEvent) {
        event.reply("You need to specify either day or week")
    }

    inner class Week : Command() {

        init {
            name = "week"
            help = "Displays a targets historical message count throughout the week."
            arguments = "target"
            cooldown = 30
            cooldownScope = CooldownScope.CHANNEL
            guildOnly = true

        }


        override fun execute(event: CommandEvent) {
            event.channel.sendTyping().queue()
            val guild = event.findArgGuildOrDefault()
            val channel = event.findArgChannelOrDefault()
            val member = event.findArgMemberOrDefault()
            if (guild != null && guild == event.guild) {
                val historyData = event.guildSettings().getTopChannelsByGuildWeekHistory()
                val chart = ChartMaker.guildTopChannelCharts(historyData)
                event.channel.sendChart(chart).queue()
            } else if (channel != null) {
                val historyData = event.guildSettings().getChannelWeekHistory(channel)
                val chart = ChartMaker.channelStatsChart(
                    "Message Activity for ${channel.name} for the past seven days",
                    "Day",
                    "Message #",
                    historyData
                )
                event.channel.sendChart(chart).queue()
            } else if (member != null) {

            } else {
                event.replyError("Unable to find any data!")
            }
        }
    }

    inner class Day : Command() {

        init {
            name = "day"
            help = "Displays a targets historical message count throughout the day"
            arguments = "target"
            cooldown = 30
            cooldownScope = CooldownScope.CHANNEL
            guildOnly = true

        }


        override fun execute(event: CommandEvent) {
            event.channel.sendTyping().queue()
            val guild = event.findArgGuildOrDefault()
            val channel = event.findArgChannelOrDefault()
            val member = event.findArgMemberOrDefault()
            if (guild != null && guild == event.guild) {
                val history = event.guildSettings().getTopChannelsByGuildDayHistory()
                val chart = ChartMaker.guildTopChannelCharts(history)
                event.channel.sendChart(chart).queue()
            } else if (channel != null) {
                val historyData = event.guildSettings().getChannelDayHistory(channel)
                val chart = ChartMaker.channelStatsChart(
                    "Message Activity for ${channel.name} for Today",
                    "Hour",
                    "Message #",
                    historyData
                )
                event.channel.sendChart(chart).queue()
            } else if (member != null) {

            } else {
                event.replyError("Unable to find any data")
            }
        }
    }

    inner class HistoricalComparision : Command() {


        init {
            name = "compare"
            help = "Displays all given channels current Day Activity"
        }


        override fun execute(event: CommandEvent) {
            event.channel.sendTyping()
            val channels = FinderUtil.findTextChannels(event.args, event.guild)
            val settings = event.guildSettings()
            if (!channels.isNullOrEmpty()) {
                val channelData = channels.map { settings.getChannelDayHistory(it) }
                val chart = ChartMaker.customChannelCharts(channelData, event.jda)
                event.channel.sendChart(chart)
            }


        }

    }

}