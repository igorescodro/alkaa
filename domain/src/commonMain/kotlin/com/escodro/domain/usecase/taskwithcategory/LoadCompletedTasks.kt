package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to get all completed tasks from the database.
 */
class LoadCompletedTasks(private val repository: TaskWithCategoryRepository) {

    /**
     * Gets all completed tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Flow<List<TaskWithCategory>> =
        repository
            .findAllTasksWithCategory()
            .map { list -> list.filter { item -> item.task.isCompleted } }

    /**
     * Gets completed tasks for a specific category.
     *
     * @param categoryId category filter
     * @return observable to be subscribe
     */
    operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> =
        repository
            .findAllTasksWithCategoryId(categoryId)
            .map { list -> list.filter { item -> item.task.isCompleted } }
}
