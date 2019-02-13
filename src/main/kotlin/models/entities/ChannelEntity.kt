package models.entities

import models.tables.ChannelsTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class ChannelEntity(cid: EntityID<Long>) : LongEntity(cid) {
    companion object : LongEntityClass<ChannelEntity>(ChannelsTable)

    var created by ChannelsTable.created
    var isTracking by ChannelsTable.isTracking
    var guild by GuildEntity referencedOn ChannelsTable.guild



}