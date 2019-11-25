package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import utils.guildSettings

class Information: Command() {

    init {
        name = "info"
        help = "Displays information on a message"
    }

    override fun execute(event: CommandEvent) {
        val settings = event.guildSettings()
        val messageEntity = settings.getMessageInformation(event.args)
        if (messageEntity != null){
            val messageId = messageEntity.id.value
            val messageAuthor = event.guild.getMemberById(messageEntity.author.id.value)
            val messageChannel = event.guild.getTextChannelById(messageEntity.channel.id.value)
            val messageJumpLink = "https://discordapp.com/channels/${event.guild.id}/${messageChannel?.id}/$messageId"
            val embed = EmbedBuilder()
                .setAuthor(messageAuthor?.effectiveName ?: "Someone", messageJumpLink, messageAuthor?.user?.avatarUrl)
                .setTimestamp(messageEntity.timestamp.toDate().toInstant())
                .setDescription(messageEntity.content)
            event.reply(embed.build())
        }
    }
}