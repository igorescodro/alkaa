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
import org.koin.dsl.module

/**
 * Repository dependency injection module.
 */
val repositoryModule = module {

    // Repositories
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
    single<TaskWithCategoryRepository> { TaskWithCategoryRepositoryImpl(get(), get()) }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get(), get()) }

    // Mappers
    factory { AlarmIntervalMapper() }
    factory { TaskMapper(get()) }
    factory { CategoryMapper() }
    factory { TaskWithCategoryMapper(get(), get()) }
    factory { AppThemeOptionsMapper() }
}
