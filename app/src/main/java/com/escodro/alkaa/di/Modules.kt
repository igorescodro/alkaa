package com.escodro.alkaa.di

import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.di.provider.DatabaseProvider
import com.escodro.alkaa.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module.
 */
val applicationModule = module {

    // Main
    viewModel { MainViewModel(get()) }
}

/**
 * Database module.
 */
val databaseModule = module {

    // Database
    single { DatabaseProvider(get()) }
    single { DaoProvider(get()) }
}

/**
 * List of all modules.
 */
val alkaaModules = listOf(applicationModule, databaseModule)
