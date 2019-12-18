package com.escodro.domain.repository

import com.escodro.domain.model.TaskWithCategory
import io.reactivex.Flowable

/**
 * Interface to represent the implementation of Task With Category repository.
 */
interface TaskWithCategoryRepository {

    /**
     * Get all inserted tasks with category.
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategory(): Flowable<List<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category
     */
    fun findAllTasksWithCategoryId(categoryId: Long): Flowable<List<TaskWithCategory>>
}
