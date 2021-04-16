package com.escodro.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.escodro.local.converter.AlarmIntervalConverter
import com.escodro.local.converter.DateConverter
import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.model.Category
import com.escodro.local.model.Task

/**
 * [Task] Database class.
 */
@Database(entities = [Task::class, Category::class], version = 4)
@TypeConverters(DateConverter::class, AlarmIntervalConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    abstract fun taskDao(): TaskDao

    /**
     * Gets the [TaskWithCategoryDao].
     *
     * @return the [TaskWithCategoryDao]
     */
    abstract fun taskWithCategoryDao(): TaskWithCategoryDao

    /**
     * Gets the [CategoryDao].
     *
     * @return the [CategoryDao]
     */
    abstract fun categoryDao(): CategoryDao
}
