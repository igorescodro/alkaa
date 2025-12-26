package com.escodro.glance.di

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.data.TaskListGlanceUpdater
import com.escodro.glance.interactor.GlanceInteractorImpl
import com.escodro.glance.mapper.TaskMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Glance dependency injection module.
 */
val glanceModule = module {

    // Presentation
    singleOf(::TaskListGlanceUpdater)

    // Interactor
    factoryOf(::GlanceInteractorImpl) bind GlanceInteractor::class

    // Mapper
    factoryOf(::TaskMapper)
}
