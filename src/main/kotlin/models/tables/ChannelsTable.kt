package models.tables

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ChannelsTable: LongIdTable() {
    val created = datetime("created")
    val isTracking = bool("tracking").default(true)
    val guild = reference("guild",GuildsTable,ReferenceOption.CASCADE)

}