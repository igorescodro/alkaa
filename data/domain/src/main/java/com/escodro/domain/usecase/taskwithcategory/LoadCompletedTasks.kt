package com.escodro.domain.usecase.taskwithcategory

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import io.reactivex.Flowable

/**
 * Use case to get all completed tasks from the database.
 */
class LoadCompletedTasks(private val repository: TaskWithCategoryRepository) {

    /**
     * Gets all completed tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Flowable<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory(isCompleted = true).applySchedulers()
}
