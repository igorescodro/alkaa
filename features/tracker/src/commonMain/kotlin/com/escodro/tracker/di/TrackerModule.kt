package com.escodro.tracker.di

import com.escodro.tracker.mapper.TrackerMapper
import com.escodro.tracker.presentation.TrackerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trackerModule = module {
    viewModelOf(::TrackerViewModel)
    factoryOf(::TrackerMapper)
}
