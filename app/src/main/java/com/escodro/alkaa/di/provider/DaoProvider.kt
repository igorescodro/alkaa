package com.escodro.alkaa.di.provider

import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao

/**
 * Repository with the database [androidx.room.Dao]s.
 */
class DaoProvider(private val database: DatabaseProvider) {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    fun getTaskDao(): TaskDao =
        database.getInstance().taskDao()

    /**
     * Gets the [CategoryDao].
     *
     * @return the [CategoryDao]
     */
    fun getCategoryDao(): CategoryDao =
        database.getInstance().categoryDao()
}
