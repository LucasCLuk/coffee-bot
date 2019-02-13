package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.core.Permission
import utils.findTextChannel
import utils.guildSettings

class Tracking : Command() {

    init {
        name = "tracking"
        aliases = arrayOf(
            "togglet"
        )
        help = "Toggles a channel's tracking status, requires Administrator permissions"
        userPermissions = arrayOf(
            Permission.ADMINISTRATOR
        )
        children = arrayOf(
            SetTracking()
        )
        arguments = "channel"
    }


    override fun execute(event: CommandEvent) {
        val channel = if (event.args != null){
            event.findTextChannel(event.args)
        } else {
            event.textChannel
        }
        val trackingStatus = event.guildSettings().isChannelIgnored(channel!!)
        when (trackingStatus) {
            true -> {
                event.reply("${event.textChannel.asMention} is **not** currently being tracked")
            }
            false -> {
                event.reply("${event.textChannel.asMention} is **currently** being tracked")
            }
        }
    }

    inner class SetTracking : Command() {

        init {
            name = "set"
            aliases = arrayOf(
                "s"
            )
            help = "Toggles the channel's tracking status. Requires Manage Channel Permissions"
            userPermissions = arrayOf(
                Permission.MANAGE_CHANNEL
            )
            arguments = "channel"
        }


        override fun execute(event: CommandEvent) {
            val guildSettingsData = event.guildSettings()
            val channel = if (event.args != null){
                event.findTextChannel(event.args)
            } else {
                event.textChannel
            }
            guildSettingsData.toggleChannelTracking(channel!!)
            val response = guildSettingsData.isChannelIgnored(channel)
            when (response) {
                true -> {
                    event.replySuccess("${event.textChannel.asMention} Is now being **Ignored**")
                }
                false -> {
                    event.replySuccess("${event.textChannel.asMention} is Now being **Tracked**")
                }
            }
        }


    }
}