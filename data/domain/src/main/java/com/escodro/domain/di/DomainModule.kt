package com.escodro.domain.di

import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.domain.usecase.alarm.implementation.CancelAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.ScheduleAlarmImpl
import com.escodro.domain.usecase.alarm.implementation.UpdateTaskAsRepeatingImpl
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.implementation.LoadAllCategoriesImpl
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.domain.usecase.search.implementation.SearchTasksByNameImpl
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskCategoryImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskDescriptionImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskStatusImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskTitleImpl
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadUncompletedTasksImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {

    @Binds
    abstract fun bindUpdateTaskStatus(impl: UpdateTaskStatusImpl): UpdateTaskStatus

    @Binds
    abstract fun bindLoadTask(impl: LoadTaskImpl): LoadTask

    @Binds
    abstract fun bindUpdateTask(impl: UpdateTaskImpl): UpdateTask

    @Binds
    abstract fun bindUpdateTaskTitle(impl: UpdateTaskTitleImpl): UpdateTaskTitle

    @Binds
    abstract fun bindUpdateTaskDescription(impl: UpdateTaskDescriptionImpl): UpdateTaskDescription

    @Binds
    abstract fun bindUpdateTaskCategory(impl: UpdateTaskCategoryImpl): UpdateTaskCategory

    @Binds
    abstract fun bindLoadAllCategories(impl: LoadAllCategoriesImpl): LoadAllCategories

    @Binds
    abstract fun bindSearchTaskByName(impl: SearchTasksByNameImpl): SearchTasksByName

    @Binds
    abstract fun bindLoadUncompletedTasks(impl: LoadUncompletedTasksImpl): LoadUncompletedTasks

    @Binds
    abstract fun bindCancelAlarm(impl: CancelAlarmImpl): CancelAlarm

    @Binds
    abstract fun bindScheduleAlarm(impl: ScheduleAlarmImpl): ScheduleAlarm

    @Binds
    abstract fun bindUpdateTaskAsRepeating(impl: UpdateTaskAsRepeatingImpl): UpdateTaskAsRepeating

    @Binds
    abstract fun bindCalendarProvider(impl: CalendarProviderImpl): CalendarProvider
}
