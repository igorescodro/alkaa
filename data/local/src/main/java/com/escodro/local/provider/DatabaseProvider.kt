package com.escodro.local.provider

import android.content.Context
import com.escodro.core.coroutines.AppCoroutineScope
import com.escodro.core.extension.getStringColor
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Category
import com.escodro.local.R
import com.escodro.local.Task
import com.escodro.local.converter.alarmIntervalAdapter
import com.escodro.local.converter.dateTimeAdapter

/**
 * Repository with the local database.
 */
class DatabaseProvider(
    private val context: Context,
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
        database ?: synchronized(this) {
            database ?: createDatabase().also { database = it }
        }

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
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        if (!databaseFile.exists()) {
            appCoroutineScope.launch {
                for (category in getDefaultCategoryList()) {
                    database.categoryQueries.insert(
                        category_name = category.category_name,
                        category_color = category.category_color,
                    )
                }
            }
        }
    }

    private fun getDefaultCategoryList() =
        listOf(
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_personal),
                category_color = context.getStringColor(R.color.blue),
            ),
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_work),
                category_color = context.getStringColor(R.color.green),
            ),
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_shopping),
                category_color = context.getStringColor(R.color.orange),
            ),
        )

    private companion object {
        private const val DATABASE_NAME = "todo-db"
    }
}
