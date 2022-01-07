package com.escodro.domain.di

import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.RescheduleFutureAlarms
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.ScheduleNextAlarm
import com.escodro.domain.usecase.alarm.ShowAlarm
import com.escodro.domain.usecase.alarm.SnoozeAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.domain.usecase.alarm.implementation.CancelAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.ScheduleAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.UpdateTaskAsRepeatingImpl
import com.escodro.domain.usecase.category.AddCategory
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.UpdateCategory
import com.escodro.domain.usecase.category.implementation.AddCategoryImpl
import com.escodro.domain.usecase.category.implementation.DeleteCategoryImpl
import com.escodro.domain.usecase.category.implementation.LoadAllCategoriesImpl
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
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    // Task Use Cases
    factory<AddTask> { AddTaskImpl(get(), get()) }
    factory { CompleteTask(get(), get(), get(), get()) }
    factory { UncompleteTask(get()) }
    factory<UpdateTaskStatus> { UpdateTaskStatusImpl(get(), get(), get(), get()) }
    factory { DeleteTask(get(), get()) }
    factory<LoadTask> { LoadTaskImpl(get()) }
    factory<UpdateTask> { UpdateTaskImpl(get(), get()) }
    factory<UpdateTaskTitle> { UpdateTaskTitleImpl(get(), get(), get()) }
    factory<UpdateTaskDescription> { UpdateTaskDescriptionImpl(get(), get()) }
    factory<UpdateTaskCategory> { UpdateTaskCategoryImpl(get(), get()) }

    // Category Use Cases
    factory<DeleteCategory> { DeleteCategoryImpl(get()) }
    factory<LoadAllCategories> { LoadAllCategoriesImpl(get()) }
    factory { LoadCategory(get()) }
    factory<AddCategory> { AddCategoryImpl(get()) }
    factory<UpdateCategory> { UpdateCategoryImpl(get()) }

    // Search Use Cases
    factory<SearchTasksByName> { SearchTasksByNameImpl(get()) }

    // Task With Category Use Cases
    factory { LoadCompletedTasks(get()) }
    factory<LoadUncompletedTasks> { LoadUncompletedTasksImpl(get()) }

    // Alarm Use Cases
    factory<CancelAlarm> { CancelAlarmImpl(get(), get()) }
    factory { RescheduleFutureAlarms(get(), get(), get(), get()) }
    factory<ScheduleAlarm> { ScheduleAlarmImpl(get(), get()) }
    factory { ScheduleNextAlarm(get(), get(), get()) }
    factory { ShowAlarm(get(), get(), get()) }
    factory { SnoozeAlarm(get(), get(), get()) }
    factory<UpdateTaskAsRepeating> { UpdateTaskAsRepeatingImpl(get()) }

    // Tracker Use Cases
    factory<LoadCompletedTasksByPeriod> { LoadCompletedTasksByPeriodImpl(get()) }

    // Preferences Use Cases
    factory { UpdateAppTheme(get()) }
    factory { LoadAppTheme(get()) }

    // Providers
    factory<CalendarProvider> { CalendarProviderImpl() }
}
