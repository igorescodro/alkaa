package com.escodro.domain.di

import com.escodro.domain.calendar.TaskCalendar
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
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {
    single { AddTask(get()) }
    single { CompleteTask(get(), get()) }
    single { UncompleteTask(get()) }
    single { UpdateTaskStatus(get(), get(), get()) }
    single { DeleteTask(get()) }
    single { GetFutureTasks(get()) }
    single { GetTask(get()) }
    single { SnoozeTask(get()) }
    single { UpdateTask(get()) }

    single { DeleteCategory(get()) }
    single { LoadAllCategories(get()) }
    single { LoadCategory(get()) }
    single { SaveCategory(get()) }

    single { LoadTasksByCategory(get(), get()) }
    single { LoadCompletedTasks(get()) }
    single { LoadUncompletedTasks(get()) }

    single { LoadCompletedTasksByPeriod(get()) }

    single { TaskCalendar() }
}
