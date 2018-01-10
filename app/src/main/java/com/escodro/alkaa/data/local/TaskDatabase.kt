package com.escodro.alkaa.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.escodro.alkaa.data.local.model.Task

/**
 * [Task] Database class.
 *
 * @author Igor Escodro on 1/2/18.
 */
@Database(entities = arrayOf(Task::class), version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}
