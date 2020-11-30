package com.escodro.task.di

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.list.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Task dependency injection module.
 */
val taskModule = module {

    // Presentation
    viewModel { TaskListViewModel(/*get(), get()*/) }

    // Mappers
    factory { AlarmIntervalMapper() }
    factory { TaskMapper(get()) }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }
}
