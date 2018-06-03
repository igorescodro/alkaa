package com.escodro.alkaa.di

import com.escodro.alkaa.ui.category.list.CategoryListAdapter
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.detail.DetailViewModel
import com.escodro.alkaa.ui.task.TaskAdapter
import com.escodro.alkaa.ui.task.TaskContract
import com.escodro.alkaa.ui.task.TaskViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

/**
 * Database module.
 *
 * @author Igor Escodro on 4/19/18.
 */
@Suppress("UnsafeCast")
val DatabaseModule = applicationContext {

    // Database
    bean { DatabaseRepository(get()) }
    bean { DaoRepository(get()) }

    // Task
    bean { TaskContract(get()) }
    viewModel { TaskViewModel(get()) }
    bean { TaskAdapter(androidApplication()) }

    // Detail
    viewModel { DetailViewModel() }

    // Category
    bean { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }
    bean { CategoryListAdapter(androidApplication()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(DatabaseModule)
