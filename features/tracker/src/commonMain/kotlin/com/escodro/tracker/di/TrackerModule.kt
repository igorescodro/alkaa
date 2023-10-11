package com.escodro.tracker.di

import com.escodro.di.viewModelDefinition
import com.escodro.tracker.mapper.TrackerMapper
import com.escodro.tracker.presentation.TrackerViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Injects the injection modules in the dynamic feature. This function needs to be called from every
 * entry point for this feature.
 */
fun injectDynamicFeature() = loadFeatureModules

private val loadFeatureModules by lazy {
    loadKoinModules(trackerModule)
}

/**
 * Tracker dependency injection module.
 */
val trackerModule = module {
    viewModelDefinition { TrackerViewModel(get(), get()) }
    factoryOf(::TrackerMapper)
}
