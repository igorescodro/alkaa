package com.escodro.local.provider

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.escodro.core.extension.getStringColor
import com.escodro.local.R
import com.escodro.local.TaskDatabase
import com.escodro.local.migration.MIGRATION_1_2
import com.escodro.local.migration.MIGRATION_2_3
import com.escodro.local.migration.MIGRATION_3_4
import com.escodro.local.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Repository with the [Room] database.
 */
internal class DatabaseProvider(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {

    private var database: TaskDatabase? = null

    /**
     * Gets an instance of [TaskDatabase].
     *
     * @return an instance of [TaskDatabase]
     */
    fun getInstance(): TaskDatabase =
        database ?: synchronized(this) {
            database ?: buildDatabase().also { database = it }
        }

    private fun buildDatabase(): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "todo-db")
            .addCallback(onCreateDatabase())
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()

    private fun onCreateDatabase() =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                coroutineScope.launch {
                    database?.categoryDao()?.insertCategory(getDefaultCategoryList())
                }
            }
        }

    private fun getDefaultCategoryList() =
        listOf(
            Category(
                name = context.getString(R.string.category_default_personal),
                color = context.getStringColor(R.color.blue)
            ),
            Category(
                name = context.getString(R.string.category_default_work),
                color = context.getStringColor(R.color.green)
            ),
            Category(
                name = context.getString(R.string.category_default_shopping),
                color = context.getStringColor(R.color.orange)
            )
        )
}
