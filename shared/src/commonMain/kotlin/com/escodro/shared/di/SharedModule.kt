package com.escodro.shared.di

import com.escodro.shared.AppViewModel
import com.escodro.shared.mapper.AppThemeOptionsMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::AppThemeOptionsMapper)
    viewModelOf(::AppViewModel)
    includes(platformSharedModule)
}

/**
 * Provides the platform-specific dependencies.
 */
internal expect val platformSharedModule: Module
