package com.escodro.alkaa.di

import com.escodro.alkaa.presentation.MainViewModel
import com.escodro.alkaa.presentation.mapper.AppThemeOptionsMapper
import com.escodro.core.extension.getAlarmManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    factory { androidContext().getAlarmManager() }

    viewModel { MainViewModel(get(), get()) }

    factory { AppThemeOptionsMapper() }
}
