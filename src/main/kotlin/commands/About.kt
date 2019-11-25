package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import org.slf4j.LoggerFactory

class About : Command() {

    init {
        name = "about"
        botPermissions = arrayOf(Permission.MESSAGE_EMBED_LINKS)
    }


    override fun execute(event: CommandEvent) {
        val oauthLink: String =
            try {
                val info = event.jda.retrieveApplicationInfo().complete()
                if (info.isBotPublic) info.getInviteUrl(0L) else ""
            } catch (e: Exception) {
                val log = LoggerFactory.getLogger("OAuth2")
                log.error("Could not generate invite link ", e)
                ""
            }
        val author = event.jda.getUserById(event.client.ownerIdLong)
        val builder = EmbedBuilder()
        builder.setDescription(
            """
            Hello, I am **${event.selfUser.name}**
            and owned by **${author?.name}#${author?.discriminator}** Feel free to [invite]($oauthLink) me to your server
            I am using ${JDAUtilitiesInfo.AUTHOR}'s [Commands Extension](${JDAUtilitiesInfo.GITHUB})
            and the [JDA library](https://github.com/DV8FromTheWorld/JDA)
            type ``${event.client.textualPrefix}${event.client.helpWord}`` to see my commands
        """.trimIndent()
        )
        builder.setColor(event.guild.selfMember.color)
        builder.setAuthor("All about ${event.selfUser.name}!", null, event.selfUser.avatarUrl)
        builder.addField(
            "Stats", """
            ${event.client.totalGuilds} Servers
            Shard ${event.jda.shardInfo.shardId + 1}/${event.jda.shardInfo.shardTotal}
        """.trimIndent(), true
        )
        builder.addField(
            "This shard", """
            ${event.jda.users.size} Users
            ${event.jda.guilds.size} Servers
        """.trimIndent(), true
        )
        builder.addField(
            "", """
            ${event.jda.textChannels.size} Text Channels
            ${event.jda.voiceChannels} Voice Channels
        """.trimIndent(), true
        )
        builder.setFooter("Last restart", null)
        builder.setTimestamp(event.client.startTime)
        event.reply(builder.build())
    }

}