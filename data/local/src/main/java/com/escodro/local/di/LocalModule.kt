package com.escodro.local.di

import com.escodro.local.datasource.CategoryLocalDataSource
import com.escodro.local.datasource.SearchLocalDataSource
import com.escodro.local.datasource.TaskLocalDataSource
import com.escodro.local.datasource.TaskWithCategoryLocalDataSource
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalModule {

    @Binds
    abstract fun bindTaskDataSource(impl: TaskLocalDataSource): TaskDataSource

    @Binds
    abstract fun bindCategoryDataSource(impl: CategoryLocalDataSource): CategoryDataSource

    @Binds
    abstract fun bindTaskWithCategoryDataSource(
        impl: TaskWithCategoryLocalDataSource
    ): TaskWithCategoryDataSource

    @Binds
    abstract fun bindSearchDataSource(impl: SearchLocalDataSource): SearchDataSource
}
