package com.escodro.local.di

import com.escodro.core.coroutines.ApplicationScope
import com.escodro.local.datasource.CategoryLocalDataSource
import com.escodro.local.datasource.SearchLocalDataSource
import com.escodro.local.datasource.TaskLocalDataSource
import com.escodro.local.datasource.TaskWithCategoryLocalDataSource
import com.escodro.local.mapper.AlarmIntervalMapper
import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.mapper.TaskMapper
import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.local.provider.DatabaseProvider
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Local dependency injection module.
 */
val localModule = module {

    // Data Sources
    singleOf(::TaskLocalDataSource) bind TaskDataSource::class
    singleOf(::CategoryLocalDataSource) bind CategoryDataSource::class
    singleOf(::TaskWithCategoryLocalDataSource) bind TaskWithCategoryDataSource::class
    singleOf(::SearchLocalDataSource) bind SearchDataSource::class

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::CategoryMapper)
    factoryOf(::TaskWithCategoryMapper)

    // Providers
    single { DatabaseProvider(context = androidContext(), coroutineScope = get(ApplicationScope)) }
    singleOf(::DaoProvider)
}
