package com.escodro.alkaa.di

import com.escodro.alkaa.presentation.MainViewModel
import com.escodro.alkaa.presentation.mapper.AppThemeOptionsMapper
import com.escodro.core.coroutines.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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

    single(ApplicationScope) { MainScope() }
}
