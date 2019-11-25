package com.escodro.local.di

import com.escodro.local.datasource.CategoryLocalDataSource
import com.escodro.local.datasource.TaskLocalDataSource
import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.mapper.TaskMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.local.provider.DatabaseProvider
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.datasource.TaskDataSource
import org.koin.dsl.module

val localModule = module {

    // Data Sources
    single<TaskDataSource> { TaskLocalDataSource(get(), get()) }
    single<CategoryDataSource> { CategoryLocalDataSource(get(), get()) }

    // Mappers
    factory { TaskMapper() }
    factory { CategoryMapper() }

    // Providers
    single { DatabaseProvider(get()) }
    single { DaoProvider(get()) }
}
