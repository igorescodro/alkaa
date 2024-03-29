package com.escodro.local.datasource

import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import com.escodro.repository.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of [TaskWithCategoryDataSource].
 */
internal class TaskWithCategoryLocalDataSource(
    private val taskWithCategoryDao: TaskWithCategoryDao,
    private val mapper: TaskWithCategoryMapper,
) : TaskWithCategoryDataSource {

    override fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>> =
        taskWithCategoryDao.findAllTasksWithCategory().map { mapper.toRepo(it) }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>> =
        taskWithCategoryDao.findAllTasksWithCategoryId(categoryId).map { mapper.toRepo(it) }
}
