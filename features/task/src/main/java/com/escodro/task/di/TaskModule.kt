package com.escodro.task.di

import com.escodro.task.presentation.detail.TaskDetailProvider
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.category.TaskCategoryViewModel
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.list.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val taskModule = module {
    viewModel { TaskListViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }

    single { TaskDetailProvider(get(), get()) }
    viewModel { TaskDetailViewModel(get()) }
    viewModel { TaskCategoryViewModel(get(), get(), get()) }
    viewModel { TaskAlarmViewModel(get(), get()) }
}
