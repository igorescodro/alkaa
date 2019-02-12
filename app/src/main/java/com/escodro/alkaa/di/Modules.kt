package com.escodro.alkaa.di

import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.di.provider.DatabaseProvider
import com.escodro.alkaa.ui.category.create.NewCategoryContract
import com.escodro.alkaa.ui.category.create.NewCategoryViewModel
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainTaskViewModel
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotification
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationChannel
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationScheduler
import com.escodro.alkaa.ui.task.detail.TaskCategoryContract
import com.escodro.alkaa.ui.task.detail.TaskCategoryViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailContract
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
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
    viewModel { MainTaskViewModel() }

    // Task
    single { TaskListContract(get()) }
    viewModel { TaskListViewModel(get(), get()) }

    // Detail
    single { TaskDetailProvider(get()) }

    single { TaskDetailContract(get()) }
    viewModel { TaskDetailViewModel(get(), get(), get()) }

    single { TaskCategoryContract(get()) }
    viewModel { TaskCategoryViewModel(get(), get()) }

    // Category
    single { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }

    // New Category
    single { NewCategoryContract(get()) }
    viewModel { NewCategoryViewModel(get()) }

    // Alarm
    single { TaskNotificationScheduler(androidContext()) }

    // Notification
    single { TaskNotificationChannel(androidContext()) }
    single { TaskNotification(androidContext(), get()) }
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
