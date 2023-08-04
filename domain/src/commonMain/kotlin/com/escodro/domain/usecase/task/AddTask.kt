package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task

/**
 * Use case to add a task from the database.
 */
interface AddTask {

    /**
     * Adds a task.
     *
     * @param task the task to be added
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task)
}
