package com.escodro.alkaa.di

import com.escodro.alkaa.mapper.CategoryMapper
import com.escodro.alkaa.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    viewModel { MainViewModel(get(), get()) }

    factory { CategoryMapper() }
}
