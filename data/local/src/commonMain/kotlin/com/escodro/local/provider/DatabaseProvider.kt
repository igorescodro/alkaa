package com.escodro.local.provider

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Task
import com.escodro.local.converter.alarmIntervalAdapter
import com.escodro.local.converter.dateTimeAdapter

/**
 * Repository with the local database.
 */
internal class DatabaseProvider(
    private val driverFactory: DriverFactory,
    private val appCoroutineScope: AppCoroutineScope,
) {

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

        prepopulateDatabase(database)
        return database
    }

    private fun prepopulateDatabase(database: AlkaaDatabase) {
        if (driverFactory.shouldPrepopulateDatabase(DATABASE_NAME)) {
            appCoroutineScope.launch {
                for (category in driverFactory.getPrepopulateData()) {
                    database.categoryQueries.insert(
                        category_name = category.category_name,
                        category_color = category.category_color,
                    )
                }
            }
        }
    }

    private companion object {
        private const val DATABASE_NAME = "todo-db"
    }
}
