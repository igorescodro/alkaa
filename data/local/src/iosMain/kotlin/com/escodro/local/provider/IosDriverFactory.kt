package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.escodro.local.AlkaaDatabase

internal class IosDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(AlkaaDatabase.Schema, databaseName)

    override fun prepopulateDatabase(
        database: AlkaaDatabase,
        databaseName: String,
    ) {
        // TODO: Implement prepopulateDatabase
    }
}
