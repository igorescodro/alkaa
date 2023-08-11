package com.escodro.local.provider

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.escodro.local.AlkaaDatabase

/**
 * Provides the platform-specific [SqlDriver] to be used in the database.
 */
class DriverFactory(private val context: Context) {

    /**
     * Creates the platform-specific [SqlDriver] to be used in the database.
     *
     * @param databaseName the database name
     *
     * @return the [SqlDriver] to be used in the database
     */
    fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(AlkaaDatabase.Schema, context, databaseName)
}
