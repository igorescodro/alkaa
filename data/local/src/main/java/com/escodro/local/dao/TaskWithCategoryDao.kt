package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.escodro.local.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * DAO class to handle all [TaskWithCategory]-related database operations.
 */
@Dao
interface TaskWithCategoryDao {

    /**
     * Get all inserted tasks with category.
     *
     * @return all inserted tasks with category
     */
    @Query(
        """SELECT * FROM task
            LEFT JOIN category ON task_category_id = category_id"""
    )
    fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category
     */
    @Query(
        """SELECT * FROM task
            LEFT JOIN category ON task_category_id = category_id
            WHERE task_category_id = :categoryId"""
    )
    fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>>

    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    @Query(
        """SELECT * FROM task
            LEFT JOIN category ON task_category_id = category_id
            WHERE task_title LIKE :query
            ORDER BY task_is_completed
        """
    )
    fun findTaskByName(query: String): Flow<List<TaskWithCategory>>
}
