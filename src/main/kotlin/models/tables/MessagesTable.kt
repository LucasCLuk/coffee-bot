package models.tables

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.joda.time.DateTime

object MessagesTable : LongIdTable() {
    val author = reference("author",MembersTable,ReferenceOption.CASCADE)
    val content = text("content")
    val guild = reference("guild",GuildsTable,ReferenceOption.CASCADE)
    val channel = reference("channel", ChannelsTable, ReferenceOption.CASCADE)
    val timestamp = datetime("timestamp").default(DateTime.now())
    val deleted = bool("deleted").default(false)
}