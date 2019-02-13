package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.core.EmbedBuilder
import utils.guildSettings
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant

class GuildStats : Command() {

    init {
        name = "stats"
        aliases = arrayOf("s")
        help = "Displays the Guild's global message statistics"
    }


    override fun execute(event: CommandEvent) {
        val startTimer = Instant.now().toEpochMilli()
        val settings = event.guildSettings()
        val messageData = settings.getGuildStats()
        val embedBuilder = EmbedBuilder()
            .setTitle("${event.guild.name} Stats")
            .addField(
                "Top Channels",
                messageData.topChannels.joinToString("\n", transform = {
                    val channel = event.guild.getTextChannelById(it.first)?.asMention ?: it.first
                    "$channel - ${it.second} Messages"
                }), false
            )
            .addField(
                "Top Authors",
                messageData.topAuthors.joinToString("\n", transform = {
                    val author = event.guild.getMemberById(it.first)?.asMention ?: it.first
                    "$author - ${it.second} Messages"
                }), false
            )
            .addField(
                "Top Emojis",
                messageData.topEmotes.joinToString("\n", transform = {
                    val emote = event.guild.getEmoteById(it.first)?.asMention ?: it.first
                    "$emote - ${it.second}"
                }), false
            )
            .setThumbnail(event.guild.iconUrl)
        val endTimer = Instant.now().toEpochMilli()
        val df = DecimalFormat("##.###")
        df.roundingMode = RoundingMode.CEILING
        embedBuilder.setFooter("Processed in ${df.format(endTimer - startTimer)} Milliseconds", null)
        event.reply(embedBuilder.build())
    }
}