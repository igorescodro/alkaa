package com.escodro.alkaa.di

import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.di.provider.DatabaseProvider
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainTaskViewModel
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import com.escodro.alkaa.ui.task.detail.alarm.TaskAlarmViewModel
import com.escodro.alkaa.ui.task.detail.category.TaskCategoryContract
import com.escodro.alkaa.ui.task.detail.category.TaskCategoryViewModel
import com.escodro.alkaa.ui.task.detail.main.TaskDetailViewModel
import com.escodro.alkaa.ui.task.list.TaskListContract
import com.escodro.alkaa.ui.task.list.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val applicationModule = module {

    // Main
    single { MainContract(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MainTaskViewModel() }

    // Task
    single { TaskListContract(get()) }
    viewModel { TaskListViewModel(get()) }

    // Task Detail
    single { TaskDetailProvider(get(), get()) }

    viewModel { TaskDetailViewModel(get()) }

    single { TaskCategoryContract(get()) }
    viewModel { TaskCategoryViewModel(get(), get()) }

    viewModel { TaskAlarmViewModel(get(), get()) }
}

/**
 * Database module.
 */
val databaseModule = module {

    // Database
    single { DatabaseProvider(get()) }
    single { DaoProvider(get()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(applicationModule, databaseModule)
