package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.doc.standard.Error
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import utils.*
import java.text.SimpleDateFormat
import java.util.*

@Error("Unable to find any data")
class ChannelStats : Command() {


    init {
        name = "cstats"
        aliases = arrayOf(
            "ss"
        )
        help = "Display's a Channels message stats"
        arguments = "target"
    }

    private fun build(target: TextChannel, event: CommandEvent): MessageEmbed {
        val settingsData = event.guildSettings()
        val channelMessageCount = settingsData.getChannelMessageCount(target.idLong)
        val messages = settingsData.getChannelMostActive(target.idLong)
        val joinDateString = SimpleDateFormat().format(Date.from(event.guild.selfMember.timeCreated.toInstant()))
        val channelMessageCountToday = settingsData.getChannelMessageCountToday(target.idLong)
        val embed = EmbedBuilder()
            .setAuthor(target.name)
            .setDescription(
                """
                Total Messages seen: **${channelMessageCount.format()}**
                Total Channel Messages Seen Today: **${channelMessageCountToday.format()}**
                Been Monitoring since: $joinDateString
            """.trimIndent()
            )
            .setColor(event.selfMember.color)
        if (!messages.isNullOrEmpty()) {
            val mostActiveUsers = mutableListOf<String>()
            var position = 0
            val medals = medalList()
            messages.forEach {
                val member = event.findMember(it.first, event.guild)
                var emoji = ""
                if (position <= medals.size) {
                    emoji = medals[position]
                }
                if (member != null) {
                    val memberMessageCountToday =
                        settingsData.getChannelMessageCountByAuthorToday(target.idLong, member.user.idLong)
                    mostActiveUsers.add("$emoji\t${member.asMention} - **${it.second.format()}** Messages Total - **${memberMessageCountToday.format()}** Today")
                    position++
                }
            }
            embed.addField("Most Active People", mostActiveUsers.joinToString("\n"), true)
        }
        return embed.build()
    }


    override fun execute(event: CommandEvent) {
        event.channel.sendTyping()
        try {
            if (event.args.isNotEmpty()) {
                val channel = event.findTextChannel(event.args)
                if (channel != null) {
                    event.reply(build(channel, event))
                }
            } else {
                event.reply(build(event.textChannel, event))
            }
        } catch (e: Exception) {
            event.reply("Unable to retrieve data")
        }
    }
}