package models

import net.dv8tion.jda.core.entities.Guild
import java.util.*

data class GuildHistorySnapshot(val guild: Guild, val history: MutableList<ChannelHistorySnapShot>) {

    val maxYSize: Double
        get() = history.map { it.maxYSize }.max() ?: 10.0

    data class ChannelHistorySnapShot(val channel: Long, val xData: MutableList<Date>, val yData: MutableList<Int>) {

        val maxYSize: Double
            get() = yData.max()?.toDouble() ?: 10.0
    }
}

data class EmojiUseSnapshot(val authorId: Long, val count: Int)
data class GuildStatsSnapshot(
    val topChannels: List<Pair<Long, Int>>,
    val topAuthors: List<Pair<Long, Int>>,
    val topEmotes: List<Pair<Long, Int>>,
    val totalMessages: Int
)