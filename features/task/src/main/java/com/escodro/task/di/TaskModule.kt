package com.escodro.task.di

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import org.koin.dsl.module

/**
 * Task dependency injection module.
 */
val taskModule = module {

    // Mappers
    factory { AlarmIntervalMapper() }
    factory { TaskMapper(get()) }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }
}
