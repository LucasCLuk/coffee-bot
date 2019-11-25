import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.command.CommandListener
import com.jagrosh.jdautilities.commons.utils.FinderUtil
import models.GuildSettingsData
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.guildSettings
import java.util.regex.Pattern

class CoffeeWatcher(private val guildSettings: MutableMap<String, GuildSettingsData>) : ListenerAdapter(),
    CommandListener {
    val EMOJI_REGEX = Pattern.compile("<a?:(.{2,32}):(\\d{17,20})>")!!


    override fun onNonCommandMessage(event: MessageReceivedEvent) {
        val settings = guildSettings[event.guild.id]
        if (settings != null) {
            if (settings.isChannelIgnored(event.textChannel.idLong)) {
                return
            } else {
                val notBot = !event.author.isBot
                val notWebhook = !event.isWebhookMessage
                val notEmpty = event.message.contentDisplay.isNotEmpty()
                val notDM = try {
                    false
                } catch (e: IllegalStateException) {
                    true
                }
                if (booleanArrayOf(notBot, notWebhook, notEmpty, notDM).all {
                        it
                    }) {
                    settings.saveMessage(event.message)
                    val matcher = EMOJI_REGEX.toRegex().findAll(event.message.contentRaw)
                    for (match in matcher) {
                        val emoji = FinderUtil.findEmotes(match.groups[0]!!.value, event.guild)
                        if (emoji.isNotEmpty()) {
                            settings.addEmoji(emoji.first(), event.channel.idLong, event.author.idLong)
                        }
                    }
                }
            }
        }
    }

    override fun onCompletedCommand(event: CommandEvent, command: Command) {
        try {
            event.guildSettings().saveCommand(event, command)
        } catch (e: Exception) {

        }
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        try {
            guildSettings[event.guild.id]?.deleteMessage(event)
        } catch (e: Exception) {

        }
    }


    override fun onTextChannelCreate(event: TextChannelCreateEvent) {
        try {
            guildSettings[event.guild.id]?.addChannel(event.channel)
        } catch (e: Exception) {

        }
    }

    override fun onTextChannelDelete(event: TextChannelDeleteEvent) {
        try {
            guildSettings[event.guild.id]?.deleteChannel(event.channel)
        } catch (e: Exception) {
        }
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        try {
            guildSettings[event.guild.id] = GuildSettingsData(event.guild)
        } catch (e: Exception) {
        }
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        try {
            guildSettings[event.guild.id]?.delete()
        } catch (e: Exception) {
        }
    }

}