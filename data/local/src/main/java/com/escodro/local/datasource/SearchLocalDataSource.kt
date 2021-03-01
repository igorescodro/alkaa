package com.escodro.local.datasource

import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchLocalDataSource(
    daoProvider: DaoProvider,
    private val taskWithCategoryMapper: TaskWithCategoryMapper
) : SearchDataSource {

    private val taskWithCategoryDao = daoProvider.getTaskWithCategoryDao()

    override suspend fun findTaskByName(query: String): Flow<List<TaskWithCategory>> {
        val enclosedQuery = "%$query%"
        val taskList = taskWithCategoryDao.findTaskByName(enclosedQuery)
        return taskList.map { taskWithCategoryMapper.toRepo(it) }
    }
}
