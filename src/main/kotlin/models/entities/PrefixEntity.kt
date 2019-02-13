package models.entities

import models.tables.PrefixesTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class PrefixEntity(pid: EntityID<Long>): LongEntity(pid) {
    companion object : LongEntityClass<PrefixEntity>(PrefixesTable)


    var prefix by PrefixesTable.prefix
    var created by PrefixesTable.created
    var guild by GuildEntity referencedOn PrefixesTable.guild



}