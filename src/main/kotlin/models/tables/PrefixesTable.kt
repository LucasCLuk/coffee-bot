package models.tables

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PrefixesTable : LongIdTable() {
    val prefix = varchar("prefix", 10)
    val created = datetime("created")
    val guild = reference("guild", GuildsTable, ReferenceOption.CASCADE)


}