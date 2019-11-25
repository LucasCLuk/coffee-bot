package models

import net.dv8tion.jda.api.entities.Guild

data class Stats(val guild: Guild, val totalMessagesSentToday: Int)