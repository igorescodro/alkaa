package com.escodro.task.di

import com.escodro.core.coroutines.ApplicationScope
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.add.AddTaskViewModel
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.list.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Task dependency injection module.
 */
val taskModule = module {

    // Presentation
    viewModel {
        TaskListViewModel(
            loadAllTasksUseCase = get(),
            updateTaskStatusUseCase = get(),
            applicationScope = get(ApplicationScope),
            taskWithCategoryMapper = get(),
        )
    }
    viewModel {
        TaskDetailViewModel(
            loadTaskUseCase = get(),
            updateTaskTitle = get(),
            updateTaskDescription = get(),
            updateTaskCategory = get(),
            coroutineDebouncer = get(),
            applicationScope = get(ApplicationScope),
            taskMapper = get(),
        )
    }
    viewModel {
        TaskAlarmViewModel(
            scheduleAlarmUseCase = get(),
            updateTaskAsRepeatingUseCase = get(),
            cancelAlarmUseCase = get(),
            applicationScope = get(ApplicationScope),
            alarmIntervalMapper = get(),
        )
    }
    viewModel { AddTaskViewModel(addTaskUseCase = get(), applicationScope = get(ApplicationScope)) }

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::CategoryMapper)
}
