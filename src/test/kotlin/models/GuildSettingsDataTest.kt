package models

import com.squareup.moshi.Moshi
import io.mockk.mockk
import net.dv8tion.jda.internal.JDAImpl
import net.dv8tion.jda.internal.entities.EmoteImpl
import net.dv8tion.jda.internal.entities.GuildImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import utils.ChartMaker
import utils.SchemaManager

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GuildSettingsDataTest {
    private val database: Database
    private val bot: JDAImpl = mockk()
    private val guild: GuildImpl = GuildImpl(bot, 92730839854493696)
    private val channel = 391988535269523478
    private val author = 92730223316959232
    private val guildSettingsData: GuildSettingsData
    private val testCache = arrayListOf(
        543239837017309205,
        543239797657960468,
        543239796651458580,
        543239404177719296,
        543234738790334485,
        543229764387733514,
        543218310196756482,
        543217983170805780,
        543217907711082536,
        543217842149916672,
        543217821249961985,
        543217601808039936,
        543217576742879262,
        543217507302113286,
        543217504332283929,
        543217466004865036,
        543217410598240266,
        543217356235735061,
        543216019876085770,
        543215995188150275,
        543215808189300737,
        543215655315570708,
        543215641671368714,
        543214799929212968,
        543214729313779742,
        543214687068749824,
        543214561613053962,
        543210916989370368,
        543210864011116564,
        543210755697410068
    )

    init {
        val config = GuildSettingsDataTest::class.java.classLoader.getResource("config.json").readText().let {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<Config>(Config::class.java)
            return@let jsonAdapter.fromJson(it)
        }!!

        database = Database.connect(
            "jdbc:postgresql://${config.database.host}:${config.database.port}/${config.database.database}",
            driver = "org.postgresql.Driver",
            user = config.database.user,
            password = config.database.password
        )
        SchemaManager.setup()
        guildSettingsData = GuildSettingsData(guild)
        guildSettingsData.addChannel(channel)
    }

    @BeforeAll
    fun `Generate Messages`() {
        for (mid in testCache) {
            guildSettingsData.saveMessage(
                mid,
                channel,
                author,
                "Hello world"
            )
        }

    }

    @AfterAll
    fun `Delete Messages`() {
        guildSettingsData.delete()
    }

    @Test
    fun `Channel Create Test`() {
        val channelEntity = guildSettingsData.getChannelEntity(channel)
        assertThat(channelEntity).isNotNull
    }

    @Test
    fun `Channel Leaderboard`() {
        val response = guildSettingsData.getChannelMostActive(channel)
        assertThat(response).isNotEmpty
        assertThat(response[0]).isNotNull
    }

    @Test
    fun `Channel Message Count`() {
        val response = guildSettingsData.getChannelMessageCount(channel)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `Get Member Last Message`() {
        val response = try {
            guildSettingsData.getLastMessageInChannelByAuthor(channel, author)
        } catch (e: NoSuchElementException) {
            null
        }
        assertThat(response).isNotNull
    }

    @Test
    fun `Get Author Guild Message Count`() {
        val response = guildSettingsData.getGuildMessageCountByAuthor(author)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `Get Author Channel Message Count`() {
        val response = guildSettingsData.getChannelMessageCountByAuthor(channel, author)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `Get Todays Channel Message Count`() {
        val response = guildSettingsData.getChannelMessageCountToday(channel)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `Get Todays Author Message Count`() {
        val response = guildSettingsData.getAuthorMessageCountToday(author)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }


    @Test
    fun `Get Todays Top Channels`() {
        val response = guildSettingsData.getTodaysTopChannels()
        assertThat(response).isNotEmpty
    }

    @Test
    fun `Get Todays Top Authors`() {
        val response = guildSettingsData.getTodaysTopAuthors()
        assertThat(response).isNotEmpty
        assertThat(response[0]).isNotNull
    }

    @Test
    fun `Get Author Todays Message Count In Specific Channel`() {
        val response = guildSettingsData.getChannelMessageCountByAuthorToday(channel, author)
        assertThat(response).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `add Guild Prefix`() {
        guildSettingsData.addGuildPrefix("!!!")
        assertThat(guildSettingsData.prefixes.contains("!!!")).isTrue()
    }


    @Test
    fun `remove Guild Prefix`() {
        if (guildSettingsData.prefixes.contains("!!!")) {
            val response = guildSettingsData.removeGuildPrefix("!!!")
            assertThat(response).isTrue()
            assertThat(guildSettingsData.prefixes.contains("!!!")).isFalse()
        }
    }

    @Test
    fun `Ignore Channel`() {
        val channelEntity = guildSettingsData.getChannelEntity(channel)
        assertThat(channelEntity).isNotNull
    }

    @Test
    fun `is Channel Ignored`() {
        assertThat(guildSettingsData.isChannelIgnored(channel)).isFalse()
    }

    @Test
    fun `Toggle Channel Ignore`() {
        guildSettingsData.toggleChannelTracking(channel)
        assertThat(guildSettingsData.isChannelIgnored(channel)).isTrue()
        guildSettingsData.toggleChannelTracking(channel)
        assertThat(guildSettingsData.isChannelIgnored(channel)).isFalse()
    }


    @Test
    fun `Get Channel Week History`() {
        val data = guildSettingsData.getChannelWeekHistory(channel)
        assertThat(data).isNotNull
        assertThat(data.xData).hasSize(7)
        val chart = ChartMaker.channelStatsChart("data", "test", "test2", data)
        assertThat(chart).isNotNull()
    }

    @Test
    fun `Get Channel Day History`() {
        val data = guildSettingsData.getChannelDayHistory(channel)
        assertThat(data).isNotNull
        val chart = ChartMaker.channelStatsChart("data", "test", "test2", data)
        assertThat(chart).isNotNull()
    }

    @Test
    fun `Get Top Channel Day History`() {
        val data = guildSettingsData.getTopChannelsByGuildDayHistory()
        assertThat(data).isNotNull
        assertThat(data.history).isNotEmpty
        val chart = ChartMaker.guildTopChannelCharts(data)
        assertThat(chart).isNotNull()
    }

    @Test
    fun `Get Top Channel Week History`() {
        val data = guildSettingsData.getTopChannelsByGuildWeekHistory()
        assertThat(data).isNotNull
        assertThat(data.history.first().xData).hasSize(7)
        val chart = ChartMaker.guildTopChannelCharts(data)
        assertThat(chart).isNotNull()
    }

    @Test
    fun `Add emote`() {
        val data = guildSettingsData
            .addEmoji(
                EmoteImpl(543237181062512661, guild)
                    .setName("Hello"), channel, author
            )
        assertThat(data).isNotNull
    }

    @Test
    fun `Get Guild Emoji Count`() {
        val data = guildSettingsData.getTopEmojis()
        assertThat(data).isNotNull
    }

    @Test
    fun `Get Guild Emoji Count By Channel`() {
        val data = guildSettingsData.getTopEmojisByChannel(channel)
        assertThat(data).isNotNull
    }


}