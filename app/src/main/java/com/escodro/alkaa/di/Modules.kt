package com.escodro.alkaa.di

import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.di.provider.DatabaseProvider
import com.escodro.alkaa.ui.TaskNotificationChannel
import com.escodro.alkaa.ui.category.create.NewCategoryContract
import com.escodro.alkaa.ui.category.create.NewCategoryViewModel
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.alarm.TaskAlarmManager
import com.escodro.alkaa.ui.task.detail.TaskDetailContract
import com.escodro.alkaa.ui.task.detail.TaskDetailViewModel
import com.escodro.alkaa.ui.task.list.TaskListContract
import com.escodro.alkaa.ui.task.list.TaskListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Application module.
 */
val applicationModule = module {

    // Main
    single { MainContract(get()) }
    viewModel { MainViewModel(get()) }

    // Task
    single { TaskListContract(get()) }
    viewModel { TaskListViewModel(get(), get()) }

    // Detail
    single { TaskDetailContract(get()) }
    viewModel { TaskDetailViewModel(get(), get()) }

    // Category
    single { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }

    // New Category
    single { NewCategoryContract(get()) }
    viewModel { NewCategoryViewModel(get()) }

    // Alarm
    single { TaskAlarmManager(androidContext()) }

    // Notification
    single { TaskNotificationChannel(androidContext()) }
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
