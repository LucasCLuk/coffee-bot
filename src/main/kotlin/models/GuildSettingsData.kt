package models

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.command.GuildSettingsProvider
import models.entities.*
import models.tables.ChannelsTable
import models.tables.EmojisTable
import models.tables.MessagesTable
import models.tables.PrefixesTable
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.MessageDeleteEvent
import net.dv8tion.jda.core.utils.MiscUtil
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import utils.betweenTodayAndTomorrow
import java.util.Date


class GuildSettingsData(
    val guild: Guild
) :
    GuildSettingsProvider {

    private var prefixEntityList: MutableMap<String, PrefixEntity>
    private var channelEntityList: MutableMap<Long, ChannelEntity>
    private var guildEntitySettings: GuildEntity = transaction {
        GuildEntity.findById(guild.idLong) ?: GuildEntity.new(guild.idLong) {}
    }
    private val guildEntity: GuildEntity
        get() = guildEntitySettings

    init {
        prefixEntityList = loadPrefixes()
        channelEntityList = loadChannelSettings()
        for (channel: TextChannel in guild.textChannels) {
            if (guild.selfMember.hasPermission(channel, Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY)) {
                if (!channelEntityList.containsKey(channel.idLong)) {
                    addChannel(channel)
                }
            }
        }

    }


    override fun getPrefixes(): MutableCollection<String> {
        val prefixes = mutableListOf<String>()
        prefixEntityList.forEach {
            prefixes.add(it.value.prefix)
        }
        return prefixes
    }

    fun refreshData() {
        prefixEntityList = loadPrefixes()
        channelEntityList = loadChannelSettings()
    }

    private fun loadChannelSettings(): MutableMap<Long, ChannelEntity> {
        val response = mutableMapOf<Long, ChannelEntity>()
        transaction {
            ChannelEntity.find {
                ChannelsTable.guild eq guild.idLong
            }.forEach {
                response[it.id.value] = it
            }
        }
        return response
    }

    private fun loadPrefixes(): MutableMap<String, PrefixEntity> {
        val response = mutableMapOf<String, PrefixEntity>()
        transaction {
            PrefixEntity.find {
                PrefixesTable.guild eq guild.idLong
            }.forEach {
                response[it.prefix] = it
            }
        }
        return response
    }

    fun isChannelIgnored(channel: Long): Boolean {
        return !channelEntityList[channel]?.isTracking!!
    }

    fun isChannelIgnored(channel: TextChannel): Boolean {
        return isChannelIgnored(channel.idLong)
    }

    fun getChannelEntity(channel: TextChannel): ChannelEntity? {
        return getChannelEntity(channel.idLong)
    }

    fun getChannelEntity(channel: Long): ChannelEntity? {
        return channelEntityList[channel]
    }

    private fun getOrCreateAuthor(author: Long): MemberEntity {
        return transaction {
            MemberEntity.findById(author) ?: MemberEntity.new(author) {}
        }
    }

    private fun getOrCreateChannel(channel: Long): ChannelEntity {
        return transaction {
            ChannelEntity.findById(channel) ?: ChannelEntity.new(channel) {
                this.guild = guildEntity
                this.created = DateTime(
                    Date.from(
                        MiscUtil.getCreationTime(
                            channel
                        ).toInstant()
                    )
                )
            }
        }
    }

    fun addChannel(channel: TextChannel): Boolean {
        return addChannel(channel.idLong)
    }

    fun addChannel(channel: Long): Boolean {
        return if (channelEntityList.containsKey(channel)) {
            true
        } else {
            channelEntityList[channel] = transaction {
                ChannelEntity.new(channel) {
                    created = DateTime(MiscUtil.getCreationTime(channel).toInstant().toEpochMilli())
                    guild = guildEntitySettings
                }
            }
            true
        }
    }

    fun deleteChannel(channel: TextChannel): Boolean {
        return deleteChannel(channel.idLong)
    }

    fun deleteChannel(channel: Long): Boolean {
        val oldChannel = channelEntityList.remove(channel)
        if (oldChannel != null) {
            transaction {
                oldChannel.delete()
            }
        }
        return true
    }


    fun toggleChannelTracking(channel: TextChannel) {
        toggleChannelTracking(channel.idLong)
    }

    fun toggleChannelTracking(channel: Long) {
        val channelData = channelEntityList[channel]
        if (channelData != null) {
            transaction {
                channelData.isTracking = !channelData.isTracking
            }
        }
    }

    fun getGuildMessageCountByAuthor(target: Long): Int {
        return try {
            transaction {
                MessagesTable.select {
                    MessagesTable.author eq target and (MessagesTable.guild eq guild.idLong)
                }.count()
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getChannelMessageCountByAuthor(channel: Long, target: Long): Int {
        return try {
            transaction {
                MessagesTable.select {
                    MessagesTable.author eq target and (MessagesTable.channel eq channel)
                }.count()
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getChannelMessageCountByAuthorToday(channel: Long, target: Long): Int {
        return transaction {
            MessagesTable.select {
                MessagesTable.author eq target and (MessagesTable.channel eq channel) and (MessagesTable.timestamp.betweenTodayAndTomorrow())
            }.count()
        }
    }

    fun getChannelMessageCount(channel: Long): Int {
        return try {
            transaction {
                MessagesTable.select {
                    MessagesTable.channel eq channel
                }.count()
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getChannelMostActive(textChannel: Long): List<Pair<String, Int>> {
        return transaction {
            MessagesTable.slice(MessagesTable.author, MessagesTable.author.count()).select {
                MessagesTable.channel eq textChannel
            }
                .groupBy(MessagesTable.author).orderBy(MessagesTable.author.count())
                .map {
                    it[MessagesTable.author].value.toString() to it[MessagesTable.author.count()]
                }.toList()
        }
    }

    fun getLastMessageInChannelByAuthor(channel: Long, target: Long): ResultRow? {
        return transaction {
            MessagesTable.selectAll().orderBy(
                MessagesTable.timestamp
            ).filter {
                it[MessagesTable.channel].value == channel && it[MessagesTable.author].value == target
            }
        }.last()
    }

    fun getChannelMessageCountToday(channel: Long): Int {
        return transaction {
            MessagesTable.select {
                MessagesTable.channel eq channel and (MessagesTable.timestamp.betweenTodayAndTomorrow())
            }.count()
        }
    }

    fun getAuthorMessageCountToday(author: Long): Int {
        return transaction {
            MessagesTable.select {
                MessagesTable.author eq author and (MessagesTable.timestamp.betweenTodayAndTomorrow())
            }.count()
        }
    }

    fun getTodaysTopChannels(): List<Pair<Long, Int>> {
        return transaction {
            MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                .select {
                    MessagesTable.timestamp.betweenTodayAndTomorrow()
                }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count())
                .map {
                    it[MessagesTable.channel].value to it[MessagesTable.channel.count()]
                }
        }
    }

    fun getTodaysTopAuthors(): List<Pair<Long, Int>> {
        return transaction {
            MessagesTable.slice(MessagesTable.author, MessagesTable.author.count())
                .select {
                    MessagesTable.timestamp.betweenTodayAndTomorrow()
                }.groupBy(MessagesTable.author).orderBy(MessagesTable.author.count())
                .map {
                    it[MessagesTable.author].value to it[MessagesTable.author.count()]
                }
        }
    }

    fun getChannelWeekHistory(channel: TextChannel): GuildHistorySnapshot.ChannelHistorySnapShot {
        return getChannelWeekHistory(channel.idLong)
    }

    fun getChannelWeekHistory(channel: Long): GuildHistorySnapshot.ChannelHistorySnapShot {
        val xData = mutableListOf<Date>()
        val yData = mutableListOf<Int>()
        val now = DateTime.now()
        for (x in 6 downTo 0) {
            val startTime =
                now.minusDays(x)
                    .minusHours(now.hourOfDay().get())
                    .minusMinutes(now.minuteOfHour)
                    .minusSeconds(now.secondOfMinute)
                    .minusMillis(now.millisOfSecond)
            val endTime = startTime.plusHours(24)
            val data = transaction {
                MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                    .select {
                        MessagesTable.timestamp.between(startTime, endTime) and (MessagesTable.channel eq channel)
                    }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count())
                    .map {
                        it[MessagesTable.channel].value to it[MessagesTable.channel.count()]
                    }
            }
            if (data.isNotEmpty()) {
                data.forEach { pair ->
                    yData.add(pair.second)
                    xData.add(startTime.toDate())
                }
            } else {
                yData.add(0)
                xData.add(startTime.toDate())
            }
        }

        return GuildHistorySnapshot.ChannelHistorySnapShot(channel, xData, yData)
    }

    fun getChannelDayHistory(channel: TextChannel): GuildHistorySnapshot.ChannelHistorySnapShot {
        return getChannelDayHistory(channel.idLong)
    }

    fun getChannelDayHistory(channel: Long): GuildHistorySnapshot.ChannelHistorySnapShot {
        val xData = mutableListOf<Date>()
        val yData = mutableListOf<Int>()
        val now = DateTime.now()
        for (x in now.hourOfDay downTo 0 step 2) {
            val startTime =
                now
                    .minusHours(x)
                    .minusMinutes(now.minuteOfHour)
                    .minusSeconds(now.secondOfMinute)
                    .minusMillis(now.millisOfSecond)
            val endTime = startTime.plusHours(2)
            val data = transaction {
                MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                    .select {
                        MessagesTable.timestamp.between(startTime, endTime) and (MessagesTable.channel eq channel)
                    }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count())
                    .map {
                        it[MessagesTable.channel].value to it[MessagesTable.channel.count()]
                    }
            }
            if (data.isNotEmpty()) {
                data.forEach { pair ->
                    yData.add(pair.second)
                    xData.add(startTime.toDate())
                }
            } else {
                yData.add(0)
                xData.add(startTime.toDate())
            }

        }
        return GuildHistorySnapshot.ChannelHistorySnapShot(channel, xData, yData)
    }

    fun getTopCategoriesDayHistory(channel: Category) {
        TODO("Implement")
    }

    fun getTopChannelsByGuildDayHistory(): GuildHistorySnapshot {
        return getTopChannelsByGuildDayHistory(this.guild)
    }

    fun getTopChannelsByGuildDayHistory(guild: Guild): GuildHistorySnapshot {
        val topChannels = transaction {
            MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                .select {
                    MessagesTable.guild eq guild.idLong and (MessagesTable.timestamp.betweenTodayAndTomorrow())
                }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count()).limit(5)
                .map {
                    it[MessagesTable.channel].value
                }
        }
        val history = mutableListOf<GuildHistorySnapshot.ChannelHistorySnapShot>()
        topChannels.forEach {
            val channelActivity = getChannelDayHistory(it)
            history.add(channelActivity)

        }
        return GuildHistorySnapshot(guild, history)
    }

    fun getTopChannelsByGuildWeekHistory(): GuildHistorySnapshot = getTopChannelsByGuildWeekHistory(guild)

    fun getTopChannelsByGuildWeekHistory(guild: Guild): GuildHistorySnapshot {
        val topChannels = transaction {
            MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                .select {
                    MessagesTable.guild eq guild.idLong and (MessagesTable.timestamp.betweenTodayAndTomorrow())
                }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count()).limit(5)
                .map {
                    it[MessagesTable.channel].value
                }
        }
        val history = mutableListOf<GuildHistorySnapshot.ChannelHistorySnapShot>()
        topChannels.forEach {
            val channelActivity = getChannelWeekHistory(it)
            history.add(channelActivity)
        }
        return GuildHistorySnapshot(guild, history)
    }

    fun getDeleteMessageCountByChannel(channel: TextChannel): Int {
        return getDeleteMessageCountByChannel(channel.idLong)
    }

    private fun getDeleteMessageCountByChannel(channel: Long): Int {
        return transaction {
            MessagesTable.select {
                MessagesTable.channel eq channel and (MessagesTable.deleted eq true)
            }.count()
        }
    }

    fun getMessageInformation(messageID: String): MessageEntity? {
        return transaction {
            MessageEntity.findById(messageID.toLong())
        }
    }

    fun getMessageInformation(messageId: Long): MessageEntity? {
        return getMessageInformation(messageId.toString())
    }

    fun getGlobalMessageStatsByChannel(): List<Pair<Long, Int>> {
        return transaction {
            MessagesTable.slice(MessagesTable.channel, MessagesTable.channel.count())
                .select {
                    MessagesTable.guild eq guild.idLong
                }.groupBy(MessagesTable.channel).orderBy(MessagesTable.channel.count()).limit(5).reversed()
                .map {
                    it[MessagesTable.channel].value to it[MessagesTable.channel.count()]
                }
        }
    }

    fun getGlobalMessageStatsByAuthor(): List<Pair<Long, Int>> {
        return transaction {
            MessagesTable.slice(MessagesTable.author, MessagesTable.author.count())
                .select {
                    MessagesTable.guild eq guild.idLong
                }.groupBy(MessagesTable.author).orderBy(MessagesTable.author.count()).limit(5).reversed()
                .map {
                    it[MessagesTable.author].value to it[MessagesTable.author.count()]
                }
        }
    }

    fun getGuildStats(): GuildStatsSnapshot {
        return GuildStatsSnapshot(
            getGlobalMessageStatsByChannel(),
            getGlobalMessageStatsByAuthor(),
            getTopEmojis()
        )
    }

    fun addGuildPrefix(prefix: String): Boolean {
        try {
            return if (prefix in prefixEntityList) {
                false
            } else {
                transaction {
                    prefixEntityList[prefix] = PrefixEntity.new {
                        this.prefix = prefix
                        this.guild = guildEntitySettings
                        this.created = DateTime()
                    }
                }
                true
            }
        } catch (e: Exception) {
            print(e)
            return false
        }
    }

    fun removeGuildPrefix(prefix: String): Boolean {
        return try {
            if (prefixEntityList.contains(prefix)) {
                val oldprefix = prefixEntityList.remove(prefix)
                transaction {
                    oldprefix?.delete()
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun saveMessage(message: Message) {
        saveMessage(message.idLong, message.channel.idLong, message.author.idLong, message.contentRaw)
    }

    fun saveMessage(messageId: Long, channelId: Long, authorId: Long, messageContent: String): MessageEntity {
        return transaction {
            MessageEntity.new(messageId) {
                author = getOrCreateAuthor(authorId)
                content = messageContent
                guild = guildEntitySettings
                channel = getOrCreateChannel(channelId)
                timestamp = DateTime(Date.from(MiscUtil.getCreationTime(messageId).toInstant()))
            }
        }
    }

    fun deleteMessage(message: MessageDeleteEvent) {
        transaction {
            val messageEntity = MessageEntity.findById(message.messageIdLong)
            if (messageEntity != null) {
                messageEntity.deleted = true
            }
        }
    }


    fun saveCommand(event: CommandEvent, command: Command) {
        transaction {
            CommandEntity.new {
                name = command.name
                guild = guildEntitySettings
                channel = getOrCreateChannel(event.channel.idLong)
                author = getOrCreateAuthor(event.author.idLong)
                timestamp = DateTime(Date.from(event.message.creationTime.toInstant()))
            }
        }
    }

    fun addEmoji(emoji: Emote, channelId: Long, authorId: Long): EmojiEntity {
        return transaction {
            EmojiEntity.new {
                name = emoji.name
                emojiId = emoji.idLong
                guild = guildEntitySettings
                timestamp = DateTime.now()
                author = getOrCreateAuthor(authorId)
                channel = getOrCreateChannel(channelId)
            }
        }
    }

    fun getTopEmojis(): List<Pair<Long, Int>> {
        return transaction {
            EmojisTable.slice(EmojisTable.emojiId, EmojisTable.emojiId.count())
                .select {
                    EmojisTable.guild.eq(guildEntitySettings.id)
                }.groupBy(EmojisTable.emojiId).orderBy(EmojisTable.emojiId.count()).limit(10).reversed()
                .map {
                    it[EmojisTable.emojiId] to it[EmojisTable.emojiId.count()]
                }
        }
    }

    fun getTopEmojisByChannel(channel: Long): List<Pair<Long, Int>> {
        return transaction {
            EmojisTable.slice(EmojisTable.emojiId, EmojisTable.emojiId.count())
                .select {
                    EmojisTable.guild.eq(guildEntitySettings.id).and(EmojisTable.channel.eq(channel))
                }.groupBy(EmojisTable.emojiId).orderBy(EmojisTable.emojiId.count()).limit(10)
                .map {
                    it[EmojisTable.emojiId] to it[EmojisTable.emojiId.count()]
                }.reversed()
        }
    }

    fun getTopEmojisByChannel(channel: TextChannel): List<Pair<Long, Int>> {
        return getTopEmojisByChannel(channel.idLong)
    }

    fun getTopEmojisByUsers(): List<EmojiUseSnapshot> {
        return transaction {
            EmojisTable.slice(EmojisTable.author, EmojisTable.author.count())
                .select {
                    EmojisTable.guild.eq(guildEntitySettings.id)
                }.groupBy(EmojisTable.author).orderBy(EmojisTable.author.count()).limit(10)
                .reversed()
                .map {
                    EmojiUseSnapshot(
                        it[EmojisTable.author].value,
                        it[EmojisTable.author.count()]
                    )
                }
        }
    }

    fun getTopEmojisByUsers(channel: TextChannel): List<EmojiUseSnapshot> {
        return transaction {
            EmojisTable.slice(EmojisTable.author, EmojisTable.author.count())
                .select {
                    EmojisTable.guild.eq(guildEntitySettings.id).and(EmojisTable.channel.eq(channel.idLong))
                }.groupBy(EmojisTable.author).orderBy(EmojisTable.author.count()).limit(10)
                .reversed()
                .map {
                    EmojiUseSnapshot(
                        it[EmojisTable.author].value,
                        it[EmojisTable.author.count()]
                    )
                }
        }
    }

    fun delete() {
        transaction {
            guildEntity.delete()
        }
    }


}