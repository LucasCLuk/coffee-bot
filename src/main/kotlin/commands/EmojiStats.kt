package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import models.EmojiUseSnapshot
import net.dv8tion.jda.api.EmbedBuilder
import utils.findArgChannelOrDefault
import utils.format
import utils.guildSettings
import java.time.Instant

class EmojiStats : Command() {

    init {
        name = "estats"
        help = "Displays a Leader-board of the top 10 emojis in this server"
        children = arrayOf(LeaderBoardDump())
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
        embed.setDescription(emojiData.joinToString("\n", transform = {
            val emote = event.guild.getEmoteById(it.first)
            "${emote?.asMention} - ${it.second.format()}"
        }))
        embed.addField("Top Emoji Users", playerEmojiStats.joinToString("\n", transform = {
            val member = event.guild.getMemberById(it.authorId)?.asMention ?: it.authorId
            "$member - ${it.count.format()}"
        }), true)

        event.reply(embed.build())
    }

    inner class LeaderBoardDump : Command() {

        init {
            name = "dump"
            help = "Dumps the leaderboard of emojis into an attachment"
        }

        override fun execute(event: CommandEvent) {
            val settingsData = event.guildSettings()
            val emojiData = settingsData.getTopEmojis(100)
            val file = createTempFile()
            file.writeText(emojiData.joinToString("\n", transform = {
                val emote = event.guild.getEmoteById(it.first)
                "${emote?.name} - ${it.second.format()}"
            }))

            event.reply(file, "EmojiLeaderBoard.txt")
            file.delete()
        }
    }

}