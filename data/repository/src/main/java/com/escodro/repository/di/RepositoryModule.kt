package com.escodro.repository.di

import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.repository.SearchRepository
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.preferences.PreferencesRepository
import com.escodro.repository.CategoryRepositoryImpl
import com.escodro.repository.PreferencesRepositoryImpl
import com.escodro.repository.SearchRepositoryImpl
import com.escodro.repository.TaskRepositoryImpl
import com.escodro.repository.TaskWithCategoryRepositoryImpl
import com.escodro.repository.mapper.AlarmIntervalMapper
import com.escodro.repository.mapper.AppThemeOptionsMapper
import com.escodro.repository.mapper.CategoryMapper
import com.escodro.repository.mapper.TaskMapper
import com.escodro.repository.mapper.TaskWithCategoryMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Repository dependency injection module.
 */
val repositoryModule = module {

    // Repositories
    singleOf(::TaskRepositoryImpl) bind TaskRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::TaskWithCategoryRepositoryImpl) bind TaskWithCategoryRepository::class
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::PreferencesRepositoryImpl) bind PreferencesRepository::class

    // Mappers
    factoryOf(::AlarmIntervalMapper)
    factoryOf(::TaskMapper)
    factoryOf(::CategoryMapper)
    factoryOf(::TaskWithCategoryMapper)
    factoryOf(::AppThemeOptionsMapper)
}
