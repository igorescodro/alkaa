package com.escodro.local.provider

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Category
import com.escodro.local.Task
import com.escodro.local.converter.alarmIntervalAdapter
import com.escodro.local.converter.dateTimeAdapter
import com.escodro.resources.Res
import com.escodro.resources.category_default_personal
import com.escodro.resources.category_default_shopping
import com.escodro.resources.category_default_work
import org.jetbrains.compose.resources.getString

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
        if (isDatabaseEmpty(database)) {
            appCoroutineScope.launch {
                for (category in getPrepopulateData()) {
                    database.categoryQueries.insert(
                        category_name = category.category_name,
                        category_color = category.category_color,
                    )
                }
            }
        }
    }

    private fun isDatabaseEmpty(database: AlkaaDatabase): Boolean = with(database) {
        categoryQueries
            .selectAll()
            .executeAsList()
            .isEmpty() &&
            taskQueries
                .selectAllTasksWithDueDate()
                .executeAsList()
                .isEmpty()
    }

    private suspend fun getPrepopulateData(): List<Category> =
        listOf(
            Category(
                category_id = 0,
                category_name = getString(Res.string.category_default_personal),
                category_color = BLUE_HEX,
            ),
            Category(
                category_id = 0,
                category_name = getString(Res.string.category_default_work),
                category_color = GREEN_HEX,
            ),
            Category(
                category_id = 0,
                category_name = getString(Res.string.category_default_shopping),
                category_color = ORANGE_HEX,
            ),
        )

    private companion object {
        private const val DATABASE_NAME = "todo-db"

        private const val BLUE_HEX = "#62A7E0"
        private const val GREEN_HEX = "#4CB27C"
        private const val ORANGE_HEX = "#F98847"
    }
}
