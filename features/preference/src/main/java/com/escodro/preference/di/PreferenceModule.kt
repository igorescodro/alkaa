package com.escodro.preference.di

import com.escodro.preference.AppThemeOptionsMapper
import com.escodro.preference.presentation.PreferenceViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Preferences dependency injection module.
 */
val preferenceModule = module {

    viewModelOf(::PreferenceViewModel)

    factoryOf(::AppThemeOptionsMapper)
}
