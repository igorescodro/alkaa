package com.escodro.local.datasource

import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchLocalDataSource(
    private val taskWithCategoryDao: TaskWithCategoryDao,
    private val taskWithCategoryMapper: TaskWithCategoryMapper,
) : SearchDataSource {

    override fun findTaskByName(query: String): Flow<List<TaskWithCategory>> {
        val enclosedQuery = "%$query%"
        val taskList = taskWithCategoryDao.findTaskByName(enclosedQuery)
        return taskList.map { taskWithCategoryMapper.toRepo(it) }
    }
}
