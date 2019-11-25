import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.examples.command.ShutdownCommand
import commands.*
import models.GuildSettingsData
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.jetbrains.exposed.sql.Database
import utils.SchemaManager


fun main(args: Array<String>) {
    Coffee().run()
}


class Coffee {

    private lateinit var jda: JDA
    private lateinit var database: Database
    private val guildSettings: MutableMap<String, GuildSettingsData> = mutableMapOf()

    fun run() {

        val config = loadConfig()

        val jdaBuilder = JDABuilder(config["discord_token"])

        database = Database.connect(
            "${config["database_url"]}",
            driver = "org.postgresql.Driver",
            user = config.getOrDefault("postgres_user", ""),
            password = config.getOrDefault("postgres_password", "")
        )
        SchemaManager.setup()
        val eventListener = CoffeeWatcher(guildSettings)
        val commands = CommandClientBuilder()
            .setOwnerId("92730223316959232")
            .setShutdownAutomatically(true)
            .setLinkedCacheSize(1000)
            .setAlternativePrefix("!!!")
            .setEmojis("<:check:518842990924529684>", "⚠", "⁉")
            .setActivity(Activity.watching("with a million eyes"))
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
        jdaBuilder.addEventListeners(eventListener, commands)
        jda = jdaBuilder.build()
        getGuildSettings()
    }

    private fun loadConfig(): Map<String, String> {
        return mapOf(
            "database_url" to (System.getenv("POSTGRES_URL") ?: ""),
            "postgres_user" to (System.getenv("POSTGRES_USER") ?: ""),
            "postgres_password" to (System.getenv("POSTGRES_PASSWORD") ?: ""),
            "discord_token" to (System.getenv("DISCORD_TOKEN") ?: "")
        )

    }


    private fun getGuildSettings() {
        jda.awaitReady()
        for (guild: Guild in jda.guilds) {
            guildSettings[guild.id] = GuildSettingsData(guild)
        }
        println("Logged in as ${jda.selfUser}")
        println("I am Ready!")
    }


}