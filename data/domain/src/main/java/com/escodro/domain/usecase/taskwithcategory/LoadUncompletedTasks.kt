package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to get all uncompleted tasks from the database.
 */
class LoadUncompletedTasks(private val repository: TaskWithCategoryRepository) {

    /**
     * Gets all uncompleted tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Flow<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory()
            .map { list -> list.filterNot { item -> item.task.completed } }
}
