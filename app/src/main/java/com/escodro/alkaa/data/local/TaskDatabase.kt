package com.escodro.alkaa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task

/**
 * [Task] Database class.
 */
@Database(entities = [Task::class, Category::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    abstract fun taskDao(): TaskDao

    /**
     * Gets the [CategoryDao].
     *
     * @return the [CategoryDao]
     */
    abstract fun categoryDao(): CategoryDao
}
