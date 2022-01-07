package com.escodro.glance.di

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.interactor.GlanceInteractorImpl
import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.presentation.TaskListGlanceViewModel
import org.koin.dsl.module

/**
 * Glance dependency injection module.
 */
val glanceModule = module {

    // Presentation
    factory { TaskListGlanceViewModel(get(), get(), get()) }

    // Interactor
    factory<GlanceInteractor> { GlanceInteractorImpl() }

    // Mapper
    factory { TaskMapper() }
}
