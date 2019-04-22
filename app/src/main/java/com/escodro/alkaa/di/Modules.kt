package com.escodro.alkaa.di

import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.di.provider.DatabaseProvider
import com.escodro.alkaa.ui.category.detail.CategoryDetailContract
import com.escodro.alkaa.ui.category.detail.CategoryDetailViewModel
import com.escodro.alkaa.ui.category.list.CategoryListContract
import com.escodro.alkaa.ui.category.list.CategoryListViewModel
import com.escodro.alkaa.ui.main.MainContract
import com.escodro.alkaa.ui.main.MainTaskViewModel
import com.escodro.alkaa.ui.main.MainViewModel
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotification
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationChannel
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationScheduler
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import com.escodro.alkaa.ui.task.detail.alarm.TaskAlarmViewModel
import com.escodro.alkaa.ui.task.detail.category.TaskCategoryContract
import com.escodro.alkaa.ui.task.detail.category.TaskCategoryViewModel
import com.escodro.alkaa.ui.task.detail.main.TaskDetailViewModel
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
    single { TaskListContract(get(), get()) }
    viewModel { TaskListViewModel(get()) }

    // Task Detail
    single { TaskDetailProvider(get()) }

    viewModel { TaskDetailViewModel(get()) }

    single { TaskCategoryContract(get()) }
    viewModel { TaskCategoryViewModel(get(), get()) }

    viewModel { TaskAlarmViewModel(get(), get()) }

    // Category List
    single { CategoryListContract(get()) }
    viewModel { CategoryListViewModel(get()) }

    // Category Detail
    single { CategoryDetailContract(get()) }
    viewModel { CategoryDetailViewModel(get()) }

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
