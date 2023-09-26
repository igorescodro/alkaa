package com.escodro.preference.di

import com.escodro.di.viewModelDefinition
import com.escodro.preference.mapper.AppThemeOptionsMapper
import com.escodro.preference.presentation.PreferenceViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Preferences dependency injection module.
 */
val preferenceModule = module {

    viewModelDefinition {
        PreferenceViewModel(
            updateThemeUseCase = get(),
            loadAppTheme = get(),
            applicationScope = get(),
            mapper = get(),
        )
    }

    factoryOf(::AppThemeOptionsMapper)
    includes(platformPreferenceModule)
}

/**
 * Provides the platform-specific dependencies.
 */
internal expect val platformPreferenceModule: Module
