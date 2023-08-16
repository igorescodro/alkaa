package com.escodro.local.provider

import android.annotation.SuppressLint
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Category
import com.escodro.local.R

/**
 * Provides the platform-specific [SqlDriver] to be used in the database.
 */
internal class AndroidDriverFactory(
    private val context: Context,
    private val appCoroutineScope: AppCoroutineScope,
) : DriverFactory {

    /**
     * Creates the platform-specific [SqlDriver] to be used in the database.
     *
     * @param databaseName the database name
     *
     * @return the [SqlDriver] to be used in the database
     */
    override fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(AlkaaDatabase.Schema, context, databaseName)

    override fun prepopulateDatabase(database: AlkaaDatabase, databaseName: String) {
        val databaseFile = context.getDatabasePath(databaseName)
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

    @SuppressLint("ResourceType")
    private fun getDefaultCategoryList() =
        listOf(
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_personal),
                category_color = context.resources.getString(R.color.blue),
            ),
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_work),
                category_color = context.getString(R.color.green),
            ),
            Category(
                category_id = 0,
                category_name = context.getString(R.string.category_default_shopping),
                category_color = context.getString(R.color.orange),
            ),
        )
}
