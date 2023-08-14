package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import com.escodro.local.AlkaaDatabase

interface DriverFactory {
    fun createDriver(databaseName: String): SqlDriver

    // TODO better document here
    fun prepopulateDatabase(database: AlkaaDatabase, databaseName: String)
}
