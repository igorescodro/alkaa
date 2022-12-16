package com.escodro.preference.di

import com.escodro.core.coroutines.ApplicationScope
import com.escodro.preference.AppThemeOptionsMapper
import com.escodro.preference.presentation.PreferenceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Preferences dependency injection module.
 */
val preferenceModule = module {

    viewModel {
        PreferenceViewModel(
            updateThemeUseCase = get(),
            loadAppTheme = get(),
            applicationScope = get(ApplicationScope),
            mapper = get(),
        )
    }

    factoryOf(::AppThemeOptionsMapper)
}
