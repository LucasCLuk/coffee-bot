package models.entities

import models.tables.MembersTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class MemberEntity(aid: EntityID<Long>): LongEntity(aid) {
    companion object: LongEntityClass<MemberEntity>(MembersTable)

//    var guild by GuildEntity referencedOn MembersTable.guild

}