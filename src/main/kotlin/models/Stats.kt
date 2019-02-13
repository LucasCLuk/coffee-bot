package models

import net.dv8tion.jda.core.entities.Guild

data class Stats(val guild: Guild, val totalMessagesSentToday: Int)