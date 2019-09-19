package com.escodro.tracker.di

import com.escodro.tracker.presentation.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackerModule = module {
    viewModel { TrackerViewModel(get()) }
}
