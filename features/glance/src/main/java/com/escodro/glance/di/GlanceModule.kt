package com.escodro.glance.di

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.interactor.GlanceInteractorImpl
import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.presentation.TaskListGlanceDataLoader
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Glance dependency injection module.
 */
val glanceModule = module {

    // Presentation
    single { TaskListGlanceDataLoader(get(), get()) }

    // Interactor
    factory<GlanceInteractor> { GlanceInteractorImpl(androidContext()) }

    // Mapper
    factory { TaskMapper() }
}
