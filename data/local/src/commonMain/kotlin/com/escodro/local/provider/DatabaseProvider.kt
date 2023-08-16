package com.escodro.local.provider

import com.escodro.local.AlkaaDatabase
import com.escodro.local.Task
import com.escodro.local.converter.alarmIntervalAdapter
import com.escodro.local.converter.dateTimeAdapter

/**
 * Repository with the local database.
 */
class DatabaseProvider(private val driverFactory: DriverFactory) {

    private var database: AlkaaDatabase? = null

    /**
     * Gets an instance of [AlkaaDatabase].
     *
     * @return an instance of [AlkaaDatabase]
     */
    fun getInstance(): AlkaaDatabase =
        database ?: createDatabase().also { database = it }

    private fun createDatabase(): AlkaaDatabase {
        val driver = driverFactory.createDriver(databaseName = DATABASE_NAME)
        val database = AlkaaDatabase(
            driver = driver,
            TaskAdapter = Task.Adapter(
                task_due_dateAdapter = dateTimeAdapter,
                task_creation_dateAdapter = dateTimeAdapter,
                task_completed_dateAdapter = dateTimeAdapter,
                task_alarm_intervalAdapter = alarmIntervalAdapter,
            ),
        )

        driverFactory.prepopulateDatabase(database = database, databaseName = DATABASE_NAME)
        return database
    }

    private companion object {
        private const val DATABASE_NAME = "todo-db"
    }
}
