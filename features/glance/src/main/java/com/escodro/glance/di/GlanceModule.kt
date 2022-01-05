package com.escodro.glance.di

import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.presentation.TaskListGlanceDataLoader
import org.koin.dsl.module

/**
 * Glance dependency injection module.
 */
val glanceModule = module {

    // Presentation
    single { TaskListGlanceDataLoader(get(), get()) }

    // Mapper
    factory { TaskMapper() }
}