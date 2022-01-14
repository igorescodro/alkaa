package com.escodro.alkaa.di

import com.escodro.alkaa.presentation.MainViewModel
import com.escodro.alkaa.presentation.mapper.AppThemeOptionsMapper
import com.escodro.core.extension.getAlarmManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    factory { androidContext().getAlarmManager() }

    factory { CoroutineScope(SupervisorJob()) }

    viewModel { MainViewModel(get(), get()) }

    factory { AppThemeOptionsMapper() }
}
