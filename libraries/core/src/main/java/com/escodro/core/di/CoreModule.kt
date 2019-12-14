package com.escodro.core.di

import com.escodro.core.viewmodel.ToolbarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Core dependency injection module.
 */
val coreModule = module {
    viewModel { ToolbarViewModel() }
}
