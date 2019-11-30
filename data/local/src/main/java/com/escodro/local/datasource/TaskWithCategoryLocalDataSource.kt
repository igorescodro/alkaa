package com.escodro.local.datasource

import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.TaskWithCategoryDataSource
import com.escodro.repository.model.TaskWithCategory
import io.reactivex.Flowable

/**
 * Local implementation of [TaskWithCategoryDataSource].
 */
internal class TaskWithCategoryLocalDataSource(
    daoProvider: DaoProvider,
    private val mapper: TaskWithCategoryMapper
) : TaskWithCategoryDataSource {

    private val taskWithCategoryDao = daoProvider.getTaskWithCategoryDao()

    override fun findAllTasksWithCategory(isCompleted: Boolean): Flowable<List<TaskWithCategory>> =
        taskWithCategoryDao.getAllTasksWithCategory(isCompleted).map { mapper.toRepo(it) }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flowable<List<TaskWithCategory>> =
        taskWithCategoryDao.getAllTasksWithCategoryId(categoryId).map { mapper.toRepo(it) }
}
