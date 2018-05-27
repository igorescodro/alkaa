package com.escodro.alkaa.di

import android.arch.persistence.room.Room
import android.content.Context
import com.escodro.alkaa.data.local.TaskDao
import com.escodro.alkaa.data.local.TaskDatabase

/**
 * Repository with the [Room] database.
 *
 * @author Igor Escodro on 4/19/18.
 */
class DatabaseRepository(private val context: Context) {

    /**
     * Gets the [Task] database.
     *
     * @return the [Task] database
     */
    fun getDatabase(): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "todo-db").build()
}

/**
 * Repository with the database [android.arch.persistence.room.Dao]s.
 *
 * @author Igor Escodro on 4/19/18.
 */
class DaoRepository(private val database: DatabaseRepository) {

    /**
     * Gets the [TaskDao].
     *
     * @return the [TaskDao]
     */
    fun getTaskDao(): TaskDao =
        database.getDatabase().taskDao()
}
