package com.escodro.local.di

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
import org.koin.dsl.module

/**
 * Local dependency injection module.
 */
val localModule = module {

    // Data Sources
    single<TaskDataSource> { TaskLocalDataSource(get(), get()) }
    single<CategoryDataSource> { CategoryLocalDataSource(get(), get()) }
    single<TaskWithCategoryDataSource> { TaskWithCategoryLocalDataSource(get(), get()) }
    single<SearchDataSource> { SearchLocalDataSource(get(), get()) }

    // Mappers
    factory { AlarmIntervalMapper() }
    factory { TaskMapper(get()) }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }

    // Providers
    single { DatabaseProvider(get(), get()) }
    single { DaoProvider(get()) }
}
