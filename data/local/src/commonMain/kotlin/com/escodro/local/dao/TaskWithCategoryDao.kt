package com.escodro.local.dao

import com.escodro.local.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * DAO class to handle all [TaskWithCategory]-related database operations.
 */
interface TaskWithCategoryDao {

    /**
     * Get all inserted tasks with category.
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>>

    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    fun findTaskByName(query: String): Flow<List<TaskWithCategory>>
}
