package com.escodro.alkaa.di

import com.escodro.alkaa.ui.task.TaskAdapter
import com.escodro.alkaa.ui.task.TaskContract
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

/**
 * Database module.
 *
 * @author Igor Escodro on 4/19/18.
 */
val DatabaseModule = applicationContext {

    bean { DatabaseRepository(get()) }
    bean { DaoRepository(get()) }

    bean { TaskAdapter(androidApplication()) }
    bean { TaskContract(get()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(DatabaseModule)
