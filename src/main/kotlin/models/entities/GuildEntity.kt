package models.entities

import models.tables.GuildsTable
import org.jetbrains.exposed.dao.*

class GuildEntity(gid: EntityID<Long>): LongEntity(gid) {
    companion object : LongEntityClass<GuildEntity>(GuildsTable)

    var tracking by GuildsTable.tracking

}