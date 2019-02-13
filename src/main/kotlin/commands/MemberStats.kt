package commands

import com.github.marlonlom.utilities.timeago.TimeAgo
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import models.tables.MessagesTable
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.utils.MiscUtil
import utils.findMember
import utils.format
import utils.guildSettings

class MemberStats : Command() {

    init {
        name = "mstats"
        aliases = arrayOf(
            "ms"
        )
        help = "Display's a persons stats across all channels in this server"
    }


    private fun build(target: Member, event: CommandEvent): MessageEmbed {
        val settings = event.guildSettings()
        val guildMessageCount = settings.getGuildMessageCountByAuthor(target.user.idLong)
        val channelMessageCount = settings.getChannelMessageCountByAuthor(event.textChannel.idLong, target.user.idLong)
        val lastMessage = try {
            settings.getLastMessageInChannelByAuthor(event.textChannel.idLong, target.user.idLong)
        } catch (e: NoSuchElementException) {
            null
        }
        val authorMessageCountToday = settings.getAuthorMessageCountToday(target.user.idLong)
        var timeAgo: String? = null
        if (lastMessage != null) {
            timeAgo =
                    TimeAgo.using(MiscUtil.getCreationTime(lastMessage[MessagesTable.id].value).toEpochSecond() * 1000)
        }
        return EmbedBuilder()
            .setAuthor(target.effectiveName, target.user.avatarUrl)
            .setColor(target.color)
            .setDescription(
                """
                Total messages sent: **${guildMessageCount.format()}**
                Total messages sent today: **${authorMessageCountToday.format()}**
                Total messages sent in ${event.textChannel.asMention}: **${channelMessageCount.format()}**
                Last message sent: **${timeAgo ?: "Never"}**
            """.trimIndent()
            )
            .build()
    }


    override fun execute(event: CommandEvent) {
        event.channel.sendTyping()
        try {
            if (event.args.isNotEmpty()) {
                val channel = event.findMember(event.args)
                if (channel != null) {
                    event.reply(build(channel, event))
                }
            } else {
                event.reply("Unable to find data regarding this channel")
            }
        } catch (e: Exception) {
            event.reply("Unable to retrieve data")
        }
    }


}