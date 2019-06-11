package com.escodro.domain.di

import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.SaveCategory
import com.escodro.domain.usecase.task.CompleteTask
import com.escodro.domain.usecase.task.GetFutureTasks
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.usecase.task.SnoozeTask
import com.escodro.domain.usecase.task.UpdateTask
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    single { GetTask(get(), get()) }
    single { GetFutureTasks(get(), get()) }
    single { UpdateTask(get(), get()) }
    single { SnoozeTask(get(), get()) }
    single { CompleteTask(get(), get()) }

    single { LoadAllCategories(get(), get()) }
    single { LoadCategory(get(), get()) }
    single { SaveCategory(get(), get()) }
    single { DeleteCategory(get(), get()) }

    single { CategoryMapper() }
    single { TaskMapper() }
}
