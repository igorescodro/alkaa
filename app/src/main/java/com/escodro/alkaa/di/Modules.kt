package com.escodro.alkaa.di

import com.escodro.alkaa.ui.category.create.NewCategoryContract
import com.escodro.alkaa.ui.category.create.NewCategoryViewModel
import com.escodro.alkaa.ui.category.list.CategoryListAdapter
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailContract
import com.escodro.alkaa.ui.task.detail.TaskDetailViewModel
import com.escodro.alkaa.ui.task.list.TaskListAdapter
import com.escodro.alkaa.ui.task.list.TaskListContract
import com.escodro.alkaa.ui.task.list.TaskListViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

/**
 * Database module.
 *
 * @author Igor Escodro on 4/19/18.
 */
@Suppress("UnsafeCast")
val databaseModule = applicationContext {

    // Database
    bean { DatabaseRepository(get()) }
    bean { DaoRepository(get()) }
}

val applicationModule = applicationContext {

    // Main
    bean { MainContract(get()) }
    viewModel { MainViewModel(get()) }

    // Task
    bean { TaskListContract(get()) }
    viewModel { TaskListViewModel(get()) }
    bean { TaskListAdapter(androidApplication()) }

    // Detail
    bean { TaskDetailContract(get()) }
    viewModel { TaskDetailViewModel(get()) }

    // Category
    bean { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }
    bean { CategoryListAdapter(androidApplication()) }

    // New Category
    bean { NewCategoryContract(get()) }
    viewModel { NewCategoryViewModel(get()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(databaseModule, applicationModule)
