package com.escodro.alkaa.di

import com.escodro.alkaa.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    viewModel { MainViewModel(get()) }
}
