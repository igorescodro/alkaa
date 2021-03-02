package com.escodro.repository.di

import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.repository.SearchRepository
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.repository.CategoryRepositoryImpl
import com.escodro.repository.SearchRepositoryImpl
import com.escodro.repository.TaskRepositoryImpl
import com.escodro.repository.TaskWithCategoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindTaskWithCategoryRepository(
        impl: TaskWithCategoryRepositoryImpl
    ): TaskWithCategoryRepository

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}
