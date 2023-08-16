package com.escodro.datastore.di

import com.escodro.datastore.datasource.PreferencesDataSourceImpl
import com.escodro.datastore.mapper.AppThemeOptionsMapper
import com.escodro.repository.datasource.PreferencesDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * DataStore dependency injection module.
 */
val dataStoreModule = module {

    // Data Source
    singleOf(::PreferencesDataSourceImpl) bind PreferencesDataSource::class

    // Mappers
    factoryOf(::AppThemeOptionsMapper)
}
