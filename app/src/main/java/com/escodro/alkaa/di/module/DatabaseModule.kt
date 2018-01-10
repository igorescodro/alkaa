package com.escodro.alkaa.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.escodro.alkaa.data.local.TaskDatabase
import dagger.Module
import dagger.Provides

/**
 * [Module] exposing the [android.arch.persistence.room.RoomDatabase] main attributes.
 *
 * @author Igor Escodro on 1/10/18.
 */
@Module
class DatabaseModule {

    @Provides
    fun provideTaskDatabase(context: Context): TaskDatabase =
            Room.databaseBuilder(context, TaskDatabase::class.java, "todo-db").build()

    @Provides
    fun provideTaskDao(database: TaskDatabase) =
            database.taskDao()
}
