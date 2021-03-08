package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a task with category by the category id from the database.
 */
interface LoadUncompletedTasksByCategory {

    /**
     * Gets a task with category by the category id if the category exists.
     *
     * @param categoryId the category id
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>>
}
