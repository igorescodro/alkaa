package com.escodro.alkaa.di

import com.escodro.alkaa.ui.category.create.NewCategoryContract
import com.escodro.alkaa.ui.category.create.NewCategoryViewModel
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailContract
import com.escodro.alkaa.ui.task.detail.TaskDetailViewModel
import com.escodro.alkaa.ui.task.list.TaskListContract
import com.escodro.alkaa.ui.task.list.TaskListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Android-related module.
 */
val androidModule = module {

    single { SystemService(androidApplication()) }
}

/**
 * Database module.
 */
@Suppress("UnsafeCast")
val databaseModule = module {

    // Database
    single { DatabaseRepository(get()) }
    single { DaoRepository(get()) }
}

/**
 * Application module.
 */
val applicationModule = module {

    // Main
    single { MainContract(get()) }
    viewModel { MainViewModel(get()) }

    // Task
    single { TaskListContract(get()) }
    viewModel { TaskListViewModel(get()) }

    // Detail
    single { TaskDetailContract(get()) }
    viewModel { TaskDetailViewModel(get()) }

    // Category
    single { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }

    // New Category
    single { NewCategoryContract(get()) }
    viewModel { NewCategoryViewModel(get()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(androidModule, databaseModule, applicationModule)
