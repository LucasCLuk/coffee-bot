package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import models.EmojiUseSnapshot
import net.dv8tion.jda.core.EmbedBuilder
import utils.findArgChannelOrDefault
import utils.guildSettings
import java.time.Instant

class EmojiStats : Command() {

    init {
        name = "estats"
        help = "Displays a Leader-board of the top 10 emojis in this server"
    }


    override fun execute(event: CommandEvent) {
        val settingsData = event.guildSettings()
        var targetChannel = event.findArgChannelOrDefault()
        val emojiData: List<Pair<Long, Int>>
        val playerEmojiStats: List<EmojiUseSnapshot>
        if (targetChannel == event.textChannel) {
            targetChannel = null
        }
        if (targetChannel != null) {
            emojiData = settingsData.getTopEmojisByChannel(targetChannel)
            playerEmojiStats = settingsData.getTopEmojisByUsers(targetChannel)

        } else {
            emojiData = settingsData.getTopEmojis()
            playerEmojiStats = settingsData.getTopEmojisByUsers()
        }
        val embed = EmbedBuilder()
        embed.setTitle("${event.guild.name} emoji stats")
        embed.setTimestamp(Instant.now())
        embed.addField("Top Emojis", emojiData.joinToString("\n", transform = {
            val emote = event.guild.getEmoteById(it.first)
            "${emote.asMention} - ${it.second}"
        }), true)
        embed.addField("Top Emoji Users", playerEmojiStats.joinToString("\n", transform = {
            val member = event.guild.getMemberById(it.authorId)?.asMention ?: it.authorId
            "$member - ${it.count}"
        }), true)

        event.reply(embed.build())
    }
}