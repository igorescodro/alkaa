package com.escodro.domain.di

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.mapper.TaskWithCategoryMapper
import com.escodro.domain.mapper.TrackerMapper
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.SaveCategory
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.CompleteTask
import com.escodro.domain.usecase.task.DeleteTask
import com.escodro.domain.usecase.task.GetFutureTasks
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.usecase.task.SnoozeTask
import com.escodro.domain.usecase.task.UncompleteTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.domain.usecase.tracker.LoadCompletedTracker
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {
    single { AddTask(get(), get(), get()) }
    single { CompleteTask(get(), get(), get()) }
    single { UncompleteTask(get(), get()) }
    single { UpdateTaskStatus(get(), get()) }
    single { DeleteTask(get(), get()) }
    single { GetFutureTasks(get(), get()) }
    single { GetTask(get(), get()) }
    single { SnoozeTask(get(), get()) }
    single { UpdateTask(get(), get()) }

    single { DeleteCategory(get(), get()) }
    single { LoadAllCategories(get(), get()) }
    single { LoadCategory(get(), get()) }
    single { SaveCategory(get(), get()) }

    single { LoadTasksByCategory(get(), get()) }
    single { LoadCompletedTasks(get(), get()) }
    single { LoadUncompletedTasks(get(), get()) }

    single { LoadCompletedTracker(get(), get()) }

    single { CategoryMapper() }
    single { TaskMapper() }
    single { TaskWithCategoryMapper(get(), get()) }
    single { TrackerMapper() }

    single { TaskCalendar() }
}
