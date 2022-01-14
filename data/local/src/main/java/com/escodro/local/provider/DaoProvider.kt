package com.escodro.local.provider

import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.local.dao.TaskWithCategoryDao

/**
 * Repository with the database [androidx.room.Dao]s.
 */
internal class DaoProvider(private val database: DatabaseProvider) {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    fun getTaskDao(): TaskDao =
        database.getInstance().taskDao()

    /**
     * Gets the [TaskWithCategoryDao].
     *
     * @return the [TaskWithCategoryDao]
     */
    fun getTaskWithCategoryDao(): TaskWithCategoryDao =
        database.getInstance().taskWithCategoryDao()

    /**
     * Gets the [CategoryDao].
     *
     * @return the [CategoryDao]
     */
    fun getCategoryDao(): CategoryDao =
        database.getInstance().categoryDao()
}
