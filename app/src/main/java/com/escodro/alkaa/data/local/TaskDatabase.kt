package com.escodro.alkaa.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task

/**
 * [Task] Database class.
 *
 * @author Igor Escodro on 1/2/18.
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
