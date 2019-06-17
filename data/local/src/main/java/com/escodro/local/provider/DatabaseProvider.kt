package com.escodro.local.provider

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.escodro.core.extension.getStringColor
import com.escodro.local.R
import com.escodro.local.TaskDatabase
import com.escodro.model.Category
import java.util.concurrent.Executors

/**
 * Repository with the [Room] database.
 */
class DatabaseProvider(private val context: Context) {

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
            .build()

    private fun onCreateDatabase() =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    database?.categoryDao()?.insertCategory(getDefaultCategoryList())?.subscribe()
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
