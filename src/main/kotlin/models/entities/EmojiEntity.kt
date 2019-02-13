package models.entities

import models.tables.EmojisTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class EmojiEntity(eid: EntityID<Long>) : LongEntity(eid) {
    companion object : LongEntityClass<EmojiEntity>(EmojisTable)

    var name by EmojisTable.name
    var emojiId by EmojisTable.emojiId
    var guild by GuildEntity referencedOn EmojisTable.guild
    var channel by ChannelEntity referencedOn EmojisTable.channel
    var author by MemberEntity referencedOn EmojisTable.author
    var timestamp by EmojisTable.timestamp

}