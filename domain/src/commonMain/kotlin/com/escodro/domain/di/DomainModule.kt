package com.escodro.domain.di

import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.provider.DateTimeProviderImpl
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.RescheduleFutureAlarms
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.ScheduleNextAlarm
import com.escodro.domain.usecase.alarm.ShowAlarm
import com.escodro.domain.usecase.alarm.SnoozeAlarm
import com.escodro.domain.usecase.alarm.UpdateAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.domain.usecase.alarm.implementation.CancelAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.ScheduleAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.UpdateAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.UpdateTaskAsRepeatingImpl
import com.escodro.domain.usecase.category.AddCategory
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.UpdateCategory
import com.escodro.domain.usecase.category.implementation.AddCategoryImpl
import com.escodro.domain.usecase.category.implementation.DeleteCategoryImpl
import com.escodro.domain.usecase.category.implementation.LoadAllCategoriesImpl
import com.escodro.domain.usecase.category.implementation.LoadCategoryImpl
import com.escodro.domain.usecase.category.implementation.UpdateCategoryImpl
import com.escodro.domain.usecase.preferences.LoadAppTheme
import com.escodro.domain.usecase.preferences.UpdateAppTheme
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.domain.usecase.search.implementation.SearchTasksByNameImpl
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.CompleteTask
import com.escodro.domain.usecase.task.DeleteTask
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UncompleteTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.domain.usecase.checklist.AddChecklistItem
import com.escodro.domain.usecase.checklist.DeleteChecklistItem
import com.escodro.domain.usecase.checklist.LoadChecklistItems
import com.escodro.domain.usecase.checklist.UpdateChecklistItem
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskCategoryImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskDescriptionImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskStatusImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskTitleImpl
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadUncompletedTasksImpl
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import com.escodro.domain.usecase.tracker.implementation.LoadCompletedTasksByPeriodImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    // Task Use Cases
    factory<AddTask> {
        AddTaskImpl(
            taskRepository = get(),
            updateAlarm = get(),
            glanceInteractor = getOrNull(),
        )
    }
    factoryOf(::CompleteTask)
    factoryOf(::UncompleteTask)
    factory<UpdateTaskStatus> {
        UpdateTaskStatusImpl(
            taskRepository = get(),
            glanceInteractor = getOrNull(),
            completeTask = get(),
            uncompleteTask = get(),
        )
    }
    factory { DeleteTask(taskRepository = get(), alarmInteractor = getOrNull()) }
    factoryOf(::LoadTaskImpl) bind LoadTask::class
    factory<UpdateTask> { UpdateTaskImpl(taskRepository = get(), glanceInteractor = getOrNull()) }
    factory<UpdateTaskTitle> {
        UpdateTaskTitleImpl(
            loadTask = get(),
            updateTask = get(),
            alarmInteractor = get(),
            glanceInteractor = getOrNull(),
        )
    }
    factoryOf(::UpdateTaskDescriptionImpl) bind UpdateTaskDescription::class
    factoryOf(::UpdateTaskCategoryImpl) bind UpdateTaskCategory::class

    // Category Use Cases
    factoryOf(::DeleteCategoryImpl) bind DeleteCategory::class
    factoryOf(::LoadAllCategoriesImpl) bind LoadAllCategories::class
    factoryOf(::LoadCategoryImpl) bind LoadCategory::class
    factoryOf(::AddCategoryImpl) bind AddCategory::class
    factoryOf(::UpdateCategoryImpl) bind UpdateCategory::class

    // Search Use Cases
    factoryOf(::SearchTasksByNameImpl) bind SearchTasksByName::class

    // Task With Category Use Cases
    factoryOf(::LoadCompletedTasks)
    factoryOf(::LoadUncompletedTasksImpl) bind LoadUncompletedTasks::class

    // Alarm Use Cases
    factoryOf(::CancelAlarmImpl) bind CancelAlarm::class
    factoryOf(::RescheduleFutureAlarms)
    factoryOf(::ScheduleAlarmImpl) bind ScheduleAlarm::class
    factoryOf(::ScheduleNextAlarm)
    factoryOf(::ShowAlarm)
    factoryOf(::SnoozeAlarm)
    factoryOf(::UpdateTaskAsRepeatingImpl) bind UpdateTaskAsRepeating::class
    factoryOf(::UpdateAlarmImpl) bind UpdateAlarm::class

    // Tracker Use Cases
    factoryOf(::LoadCompletedTasksByPeriodImpl) bind LoadCompletedTasksByPeriod::class

    // Preferences Use Cases
    factoryOf(::UpdateAppTheme)
    factoryOf(::LoadAppTheme)

    // Checklist Use Cases
    factoryOf(::AddChecklistItem)
    factoryOf(::DeleteChecklistItem)
    factoryOf(::LoadChecklistItems)
    factoryOf(::UpdateChecklistItem)

    // Providers
    factoryOf(::DateTimeProviderImpl) bind DateTimeProvider::class
}
