package models

data class Config(val database: Database, val token: String) {


    data class Database(
        val host: String,
        val user: String,
        val password: String,
        val database: String,
        val port: String
    )
}