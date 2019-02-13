package models.tables

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object EmojisTable : LongIdTable() {
    val name = varchar("name", 256)
    val timestamp = EmojisTable.datetime("timestamp")
    val guild = EmojisTable.reference("guild", GuildsTable, ReferenceOption.CASCADE)
    val channel = EmojisTable.reference("channel", ChannelsTable, ReferenceOption.CASCADE)
    val author = EmojisTable.reference("author", MembersTable, ReferenceOption.CASCADE)
    val emojiId = long("emojiId")

}