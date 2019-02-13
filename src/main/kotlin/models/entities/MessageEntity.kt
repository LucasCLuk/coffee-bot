package models.entities

import models.tables.MessagesTable
import org.jetbrains.exposed.dao.*

class MessageEntity(eid: EntityID<Long>) : LongEntity(eid) {
    companion object : LongEntityClass<MessageEntity>(MessagesTable)

    var author by MemberEntity referencedOn MessagesTable.author
    var content by MessagesTable.content
    var guild by GuildEntity referencedOn MessagesTable.guild
    var channel by ChannelEntity referencedOn MessagesTable.channel
    var timestamp by MessagesTable.timestamp
    var deleted by MessagesTable.deleted

}
