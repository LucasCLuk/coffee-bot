package models.entities

import models.tables.CommandsTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class CommandEntity(cid: EntityID<Int>) : IntEntity(cid) {
    companion object : IntEntityClass<CommandEntity>(CommandsTable)

    var name by CommandsTable.name
    var timestamp by CommandsTable.timestamp
    var guild by GuildEntity referencedOn  CommandsTable.guild
    var channel by ChannelEntity referencedOn CommandsTable.channel
    var author by MemberEntity referencedOn CommandsTable.author

}