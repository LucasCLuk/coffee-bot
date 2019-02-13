package models.tables

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object CommandsTable: IntIdTable() {
    val name = varchar("name",256)
    val timestamp = datetime("timestamp")
    val guild = reference("guild",GuildsTable,ReferenceOption.CASCADE)
    val channel = reference("channel",ChannelsTable,ReferenceOption.CASCADE)
    val author = reference("author",MembersTable,ReferenceOption.CASCADE)
}