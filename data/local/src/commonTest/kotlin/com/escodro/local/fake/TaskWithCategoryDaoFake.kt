package com.escodro.local.fake

import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class TaskWithCategoryDaoFake : TaskWithCategoryDao {

    var searchQuery: String? = null
    override fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>> {
        TODO("Not yet implemented")
    }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>> {
        TODO("Not yet implemented")
    }

    override fun findTaskByName(query: String): Flow<List<TaskWithCategory>> {
        searchQuery = query
        return flow { }
    }

    fun clear() {
        searchQuery = null
    }
}
