package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import models.GuildSettingsData
import net.dv8tion.jda.api.EmbedBuilder
import utils.guildSettings

class Prefix : Command() {

    init {
        name = "prefix"
        children = arrayOf(
            SetPrefix(),
            DeletePrefix()
        )
        aliases = arrayOf(
            "p"
        )
        help = "Manages the bot's prefix for this server"

    }


    override fun execute(event: CommandEvent) {
        val prefixes = event.client.getSettingsFor<GuildSettingsData>(event.guild).prefixes
        if (prefixes.isNotEmpty()){
            val prefixString = prefixes.joinToString("\n")
            val embed = EmbedBuilder()
                .setDescription("I have ${prefixes.size} Prefixes,\n$prefixString")
                .build()
            event.reply(embed)
        } else {
            event.replyError("I have no custom prefixes set for this server.")
        }
    }


    inner class SetPrefix : Command() {

        init {
            name = "add"
            aliases = arrayOf(
                "set"
            )
            help = "Adds a prefix for the bot to listen to for this server. A server can have as many prefixes as desired."
            arguments = "prefix"
        }

        override fun execute(event: CommandEvent) {
            val settings = event.guildSettings()
            if (event.args.isNotBlank()) {
                val newPRefix = settings.addGuildPrefix(event.args)
                if (newPRefix) {
                    event.replySuccess("Prefix added")
                }
            }
        }
    }

    inner class DeletePrefix: Command() {


        init {
            name = "delete"
            aliases = arrayOf(
                "del",
                "d",
                "remove"
            )
            arguments = "prefix"
            help = "Remove's a prefix from the bot's database"
        }

        override fun execute(event: CommandEvent) {
            val settings = event.guildSettings()
            if (event.args.isNotBlank()){
                settings.removeGuildPrefix(event.args)
                event.replySuccess("Deleted Prefix")
            } else {
                event.replyError("Unable to delete prefix")
            }
        }
    }
}