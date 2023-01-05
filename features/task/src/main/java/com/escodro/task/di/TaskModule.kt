package com.escodro.task.di

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.add.AddTaskViewModel
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.list.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Task dependency injection module.
 */
val taskModule = module {

    // Presentation
    viewModelOf(::TaskListViewModel)
    viewModelOf(::TaskDetailViewModel)
    viewModelOf(::TaskAlarmViewModel)
    viewModelOf(::AddTaskViewModel)

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::CategoryMapper)
}
