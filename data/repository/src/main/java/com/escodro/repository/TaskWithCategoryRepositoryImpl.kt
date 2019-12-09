package com.escodro.repository

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import com.escodro.repository.mapper.TaskWithCategoryMapper
import io.reactivex.Flowable

internal class TaskWithCategoryRepositoryImpl(
    private val dataSource: TaskWithCategoryDataSource,
    private val mapper: TaskWithCategoryMapper
) : TaskWithCategoryRepository {

    override fun findAllTasksWithCategory(isCompleted: Boolean): Flowable<List<TaskWithCategory>> =
        dataSource.findAllTasksWithCategory(isCompleted).map { mapper.toDomain(it) }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flowable<List<TaskWithCategory>> =
        dataSource.findAllTasksWithCategoryId(categoryId).map { mapper.toDomain(it) }
}
