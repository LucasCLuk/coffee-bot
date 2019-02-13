package models.tables

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.LongIdTable

object GuildsTable : LongIdTable() {
    val tracking = bool("tracking").default(true)
}