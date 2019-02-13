package utils

import models.tables.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object SchemaManager {


    fun setup() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                MessagesTable,
                PrefixesTable,
                CommandsTable,
                ChannelsTable,
                EmojisTable
            )
        }
    }
}