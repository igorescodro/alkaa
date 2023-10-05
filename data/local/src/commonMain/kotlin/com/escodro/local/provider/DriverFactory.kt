package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import com.escodro.local.Category

/**
 * Provides the platform-specific [SqlDriver] to be used in the database.
 */
internal interface DriverFactory {

    /**
     * Creates the platform-specific [SqlDriver] to be used in the database.
     *
     * @param databaseName the database name
     *
     * @return the [SqlDriver] to be used in the database
     */
    fun createDriver(databaseName: String): SqlDriver

    /**
     * Checks if the database is opening for the first time and should be prepopulated.
     *
     * @param databaseName the database name
     *
     * @return true if the database should be prepopulated, false otherwise
     */
    fun shouldPrepopulateDatabase(databaseName: String): Boolean

    /**
     * Gets the prepopulate data to be inserted in the database.
     *
     * @return the prepopulate data
     */
    fun getPrepopulateData(): List<Category>
}
