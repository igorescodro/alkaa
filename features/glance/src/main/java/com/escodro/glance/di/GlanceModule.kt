package com.escodro.glance.di

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.interactor.GlanceInteractorImpl
import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.presentation.TaskListGlanceViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Glance dependency injection module.
 */
val glanceModule = module {

    // Presentation
    factoryOf(::TaskListGlanceViewModel)

    // Interactor
    factoryOf(::GlanceInteractorImpl) bind GlanceInteractor::class

    // Mapper
    factoryOf(::TaskMapper)
}
