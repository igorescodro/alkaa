package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.escodro.local.model.TaskWithCategory
import io.reactivex.Flowable

/**
 * DAO class to handle all [TaskWithCategory]-related database operations.
 */
@Dao
interface TaskWithCategoryDao {

    /**
     * Get all inserted tasks with category.
     *
     * @param isCompleted `false` to show all the completed tasks, `false` otherwise
     *
     * @return all inserted tasks with category
     */
    @Query(
        """SELECT * FROM task
            LEFT JOIN category ON task_category_id = category_id
            WHERE task_is_completed = :isCompleted"""
    )
    fun getAllTasksWithCategory(isCompleted: Boolean): Flowable<List<TaskWithCategory>>

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
            WHERE task_category_id = :categoryId
            AND task_is_completed = 0"""
    )
    fun getAllTasksWithCategoryId(categoryId: Long): Flowable<List<TaskWithCategory>>
}
