package com.escodro.repository.datasource

import com.escodro.repository.model.TaskWithCategory
import io.reactivex.Flowable

/**
 * Interface to represent the implementation of Task With Category data source.
 */
interface TaskWithCategoryDataSource {

    /**
     * Get all inserted tasks with category.
     *
     * @param isCompleted `false` to show all the completed tasks, `false` otherwise
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategory(isCompleted: Boolean): Flowable<List<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategoryId(categoryId: Long): Flowable<List<TaskWithCategory>>
}
