package com.escodro.alkaa.di

import com.escodro.alkaa.presentation.MainViewModel
import com.escodro.alkaa.presentation.mapper.AppThemeOptionsMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    factory { CoroutineScope(SupervisorJob()) }

    viewModelOf(::MainViewModel)

    factoryOf(::AppThemeOptionsMapper)
}
