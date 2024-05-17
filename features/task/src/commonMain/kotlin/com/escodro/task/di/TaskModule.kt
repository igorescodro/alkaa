package com.escodro.task.di

import com.escodro.di.viewModelDefinition
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.add.AddTaskViewModel
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmSchedulerImpl
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.list.TaskListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Task dependency injection module.
 */
val taskModule = module {

    // Presentation
    viewModelDefinition {
        TaskListViewModel(
            loadAllTasksUseCase = get(),
            updateTaskStatusUseCase = get(),
            applicationScope = get(),
            taskWithCategoryMapper = get(),
        )
    }
    viewModelDefinition {
        TaskDetailViewModel(
            loadTaskUseCase = get(),
            updateTaskTitle = get(),
            updateTaskDescription = get(),
            updateTaskCategory = get(),
            coroutineDebouncer = get(),
            applicationScope = get(),
            taskMapper = get(),
        )
    }
    viewModelDefinition {
        TaskAlarmViewModel(
            updateAlarm = get(),
            loadTask = get(),
            applicationScope = get(),
            alarmIntervalMapper = get(),
        )
    }
    viewModelDefinition {
        AddTaskViewModel(
            addTaskUseCase = get(),
            alarmIntervalMapper = get(),
            applicationScope = get(),
        )
    }

    factoryOf(::OpenAlarmSchedulerImpl) bind OpenAlarmScheduler::class

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::CategoryMapper)

    includes(platformTaskModule)
}

/**
 * Provides the platform-specific dependencies.
 */
expect val platformTaskModule: Module
