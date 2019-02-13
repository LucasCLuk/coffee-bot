import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.examples.command.ShutdownCommand
import com.squareup.moshi.Moshi
import commands.*
import models.Config
import models.GuildSettingsData
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.Guild
import org.jetbrains.exposed.sql.Database
import utils.SchemaManager
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths


fun main(args: Array<String>) {
    Coffee().run()
}


class Coffee {

    private lateinit var jda: JDA
    private lateinit var database: Database
    private val guildSettings: MutableMap<String, GuildSettingsData> = mutableMapOf()

    fun run() {

        val config = loadConfig()

        val jdaBuilder = JDABuilder(config.token)

        database = Database.connect(
            "jdbc:postgresql://${config.database.host}:${config.database.port}/${config.database.database}",
            driver = "org.postgresql.Driver",
            user = config.database.user,
            password = config.database.password
        )
        SchemaManager.setup()
        val eventListener = CoffeeWatcher(guildSettings)
        val commands = CommandClientBuilder()
            .setOwnerId("92730223316959232")
            .setShutdownAutomatically(true)
            .setLinkedCacheSize(1000)
            .setAlternativePrefix("!!!")
            .setEmojis("<:check:518842990924529684>", "⚠", "⁉")
            .setGame(Game.of(Game.GameType.WATCHING, "with a million eyes"))
            .setGuildSettingsManager {
                guildSettings[it.id]
            }
            .setListener(eventListener)
            .addCommands(
                About(),
                ShutdownCommand(),
                ChannelStats(),
                MemberStats(),
                EmojiStats(),
                GuildStats(),
                Prefix(),
                Hello(),
                Tracking(),
                History(),
                Information(),
                Eval()
            )
            .build()
        jdaBuilder.addEventListener(eventListener, commands)
        jda = jdaBuilder.build()
        getGuildSettings()
    }

    private fun loadConfig(): Config {
        val cwd = Paths.get("").toAbsolutePath().toString()
        val configFile = File("$cwd/config.json")
        if (configFile.exists()) {
            return File("$cwd/config.json").readText().let {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter<Config>(Config::class.java)
                return@let jsonAdapter.fromJson(it)
            }!!
        } else {
            throw FileNotFoundException("config.json file not found")
        }
    }


    private fun getGuildSettings() {
        jda.awaitReady()
        for (guild: Guild in jda.guilds) {
            guildSettings[guild.id] = GuildSettingsData(guild)
        }
        println("I am Ready!")
    }


}