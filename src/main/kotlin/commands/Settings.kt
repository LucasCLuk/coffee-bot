package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

class Settings : Command() {


    init {
        name = "settings"
        children = arrayOf(
            Prefix(), Tracking()
        )
    }


    override fun execute(event: CommandEvent) {

    }
}