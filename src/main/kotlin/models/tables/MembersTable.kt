package models.tables

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object MembersTable: LongIdTable() {
//    val guild = reference("guild",GuildsTable,ReferenceOption.CASCADE).primaryKey().uniqueIndex("MemberGuild")

}