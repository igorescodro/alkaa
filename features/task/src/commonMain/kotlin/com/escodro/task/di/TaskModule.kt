package com.escodro.task.di

import com.escodro.navigationapi.provider.NavGraph
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.navigation.TaskNavGraph
import com.escodro.task.presentation.add.AddTaskViewModel
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmSchedulerImpl
import com.escodro.task.presentation.detail.main.TaskDetailScreenImpl
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.list.TaskListViewModel
import com.escodro.taskapi.TaskDetailScreen
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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

    factoryOf(::OpenAlarmSchedulerImpl) bind OpenAlarmScheduler::class

    factoryOf(::TaskDetailScreenImpl) bind TaskDetailScreen::class

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::CategoryMapper)

    // Navigation
    factoryOf(::TaskNavGraph) bind NavGraph::class

    includes(platformTaskModule)
}

/**
 * Provides the platform-specific dependencies.
 */
expect val platformTaskModule: Module
