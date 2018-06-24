package com.escodro.alkaa.di

import android.arch.persistence.room.Room
import android.content.Context
import com.escodro.alkaa.data.local.TaskDatabase
import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao

/**
 * Repository with the [Room] database.
 */
class DatabaseRepository(private val context: Context) {

    /**
     * Gets the application database.
     *
     * @return the application database
     */
    fun getDatabase(): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "todo-db").build()
}

/**
 * Repository with the database [android.arch.persistence.room.Dao]s.
 */
class DaoRepository(private val database: DatabaseRepository) {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    fun getTaskDao(): TaskDao =
        database.getDatabase().taskDao()

    /**
     * Gets the [CategoryDao].
     *
     * @return the [CategoryDao]
     */
    fun getCategoryDao(): CategoryDao =
        database.getDatabase().categoryDao()
}
