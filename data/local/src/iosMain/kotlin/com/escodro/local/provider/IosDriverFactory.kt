package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import com.escodro.local.AlkaaDatabase

internal class IosDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        TODO("Not yet implemented")
    }

    override fun prepopulateDatabase(
        database: AlkaaDatabase,
        databaseName: String,
    ) {
        TODO("Not yet implemented")
    }
}
