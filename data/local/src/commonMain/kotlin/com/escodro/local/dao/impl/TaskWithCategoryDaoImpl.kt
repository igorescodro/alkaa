package com.escodro.local.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.escodro.local.SelectAllTasksWithCategory
import com.escodro.local.TaskWithCategoryQueries
import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.mapper.SelectMapper
import com.escodro.local.model.TaskWithCategory
import com.escodro.local.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [TaskWithCategoryDao]. The mappers were forced to use the same model because
 * they all have the same structure. However, SQLDelight creates a model for each of the queries
 * which requires one mapper for each of them.
 */
internal class TaskWithCategoryDaoImpl(
    private val databaseProvider: DatabaseProvider,
    private val selectMapper: SelectMapper,
) : TaskWithCategoryDao {

    private val taskWithCategoryQueries: TaskWithCategoryQueries
        get() = databaseProvider.getInstance().taskWithCategoryQueries

    override fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>> =
        taskWithCategoryQueries
            .selectAllTasksWithCategory(mapper = ::SelectAllTasksWithCategory)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map(selectMapper::toTaskWithCategory)

    override fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>> =
        taskWithCategoryQueries
            .selectAllTasksWithCategoryId(
                task_category_id = categoryId,
                mapper = ::SelectAllTasksWithCategory,
            ).asFlow()
            .mapToList(Dispatchers.IO)
            .map(selectMapper::toTaskWithCategory)

    override fun findTaskByName(query: String): Flow<List<TaskWithCategory>> =
        taskWithCategoryQueries
            .selectTaskByName(query = query, mapper = ::SelectAllTasksWithCategory)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map(selectMapper::toTaskWithCategory)
}
