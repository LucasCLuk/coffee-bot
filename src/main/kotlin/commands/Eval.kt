package commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.core.EmbedBuilder
import utils.Emoji
import utils.cleanupCode
import utils.randomColor
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.script.ScriptEngineManager
import javax.script.ScriptException


class Eval : Command() {

    init {
        name = "eval"
        aliases = arrayOf(
            "e"
        )
        ownerCommand = true
    }

    override fun execute(event: CommandEvent) {

        val engine = ScriptEngineManager().getEngineByName("nashorn")

        /* Imports */
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util);")
        } catch (ex: ScriptException) {
            ex.printStackTrace()
        }

        /* Put string representations */
        engine.put("aibot", event.jda::class.java)
        engine.put("jda", event.jda)
        engine.put("api", event.jda)

        engine.put("message", event.message)
        engine.put("guild", event.guild)
        engine.put("server", event.guild)
        engine.put("channel", event.channel)
        engine.put("tc", event.textChannel)
        engine.put("pm", event.privateChannel)
        engine.put("vc", event.member.voiceState.channel)

        engine.put("author", event.author)
        engine.put("member", event.member)
        engine.put("self", event.guild.selfMember.user)
        engine.put("selfmem", event.guild.selfMember)

        val service = Executors.newScheduledThreadPool(1)

        val future = service.schedule({

            /* Initialize Objects */
            val startExec = System.currentTimeMillis()
            var out: Any? = null
            val message = EmbedBuilder()
                .setColor(randomColor())
                .setAuthor("AIBot Eval", null, event.guild.selfMember.user.effectiveAvatarUrl)
                .setFooter("Bot Owner " + event.member.effectiveName + " Only", event.author.effectiveAvatarUrl)
                .setTimestamp(Instant.now())

            try {
                /* Build input script */
                val script = cleanupCode(event.args)
                message.addField(Emoji.IN + " Input", "```java\n\n$script```", false)

                /* Output */
                out = engine.eval(script)
                message.addField(Emoji.OUT + " Output", "```java\n\n" + out!!.toString() + "```", false)

                /* Exception */
            } catch (ex: Exception) {
                message.addField(Emoji.ERROR + " Error", "```java\n\n" + ex.message + "```", false)
            }

            /* Timer */
            message.addField(
                Emoji.STOPWATCH + " Timing",
                (System.currentTimeMillis() - startExec).toString() + " milliseconds",
                false
            )
            event.channel.sendMessage(message.build()).queue()

            service.shutdownNow()

        }, 0, TimeUnit.MILLISECONDS)

    }

}