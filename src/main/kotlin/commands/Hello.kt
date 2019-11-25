package commands

import com.github.marlonlom.utilities.timeago.TimeAgo
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent


class Hello : Command() {

    init {
        name = "hello"
        aliases = arrayOf(
            "hi",
            "yo"
        )
        help = "A Hello command"
    }

    override fun execute(event: CommandEvent) {
        val ago = TimeAgo.using(event.client.startTime.toEpochSecond() * 1000)
        val owner = event.jda.getUserById(event.client.ownerId)
        event.replySuccess(
            "Hello! I am a bot written by ${owner?.name}#${owner?.discriminator}.\n" +
                    "I've been up since $ago"
        )
    }
}