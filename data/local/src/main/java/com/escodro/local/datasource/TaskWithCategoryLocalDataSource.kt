package com.escodro.local.datasource

import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import com.escodro.repository.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of [TaskWithCategoryDataSource].
 */
internal class TaskWithCategoryLocalDataSource(
    daoProvider: DaoProvider,
    private val mapper: TaskWithCategoryMapper
) : TaskWithCategoryDataSource {

    private val taskWithCategoryDao = daoProvider.getTaskWithCategoryDao()

    override fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>> =
        taskWithCategoryDao.findAllTasksWithCategory().map { mapper.toRepo(it) }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>> =
        taskWithCategoryDao.findAllTasksWithCategoryId(categoryId).map { mapper.toRepo(it) }
}
