package com.escodro.local.di

import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.dao.impl.CategoryDaoImpl
import com.escodro.local.dao.impl.TaskDaoImpl
import com.escodro.local.dao.impl.TaskWithCategoryDaoImpl
import com.escodro.local.datasource.CategoryLocalDataSource
import com.escodro.local.datasource.SearchLocalDataSource
import com.escodro.local.datasource.TaskLocalDataSource
import com.escodro.local.datasource.TaskWithCategoryLocalDataSource
import com.escodro.local.dao.ChecklistDao
import com.escodro.local.dao.impl.ChecklistDaoImpl
import com.escodro.local.datasource.ChecklistLocalDataSource
import com.escodro.local.mapper.ChecklistItemMapper
import com.escodro.repository.datasource.ChecklistDataSource
import com.escodro.local.mapper.AlarmIntervalMapper
import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.mapper.SelectMapper
import com.escodro.local.mapper.TaskMapper
import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DatabaseProvider
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import org.koin.core.module.Module
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
    singleOf(::ChecklistLocalDataSource) bind ChecklistDataSource::class

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::CategoryMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::SelectMapper)
    factoryOf(::ChecklistItemMapper)

    // DAOs
    singleOf(::CategoryDaoImpl) bind CategoryDao::class
    singleOf(::TaskDaoImpl) bind TaskDao::class
    singleOf(::ChecklistDaoImpl) bind ChecklistDao::class
    singleOf(::TaskWithCategoryDaoImpl) bind TaskWithCategoryDao::class

    // Providers
    singleOf(::DatabaseProvider)
    includes(platformLocalModule)
}

/**
 * Provides the platform-specific dependencies.
 */
internal expect val platformLocalModule: Module
