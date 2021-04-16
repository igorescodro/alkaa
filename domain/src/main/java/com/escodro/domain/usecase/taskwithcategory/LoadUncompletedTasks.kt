package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get all uncompleted tasks from the database.
 */
interface LoadUncompletedTasks {

    /**
     * Gets all uncompleted tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(categoryId: Long? = null): Flow<List<TaskWithCategory>>
}
