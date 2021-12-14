package com.escodro.datastore.di

import com.escodro.datastore.datasource.PreferencesDataSourceImpl
import com.escodro.datastore.mapper.AppThemeOptionsMapper
import com.escodro.repository.datasource.PreferencesDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * DataStore dependency injection module.
 */
val dataStoreModule = module {

    // Data Source
    factory<PreferencesDataSource> { PreferencesDataSourceImpl(androidContext(), get()) }

    // Mappers
    factory { AppThemeOptionsMapper() }
}
