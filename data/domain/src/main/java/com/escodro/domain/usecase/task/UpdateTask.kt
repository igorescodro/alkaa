package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to update a task from the database.
 */
class UpdateTask(private val taskRepository: TaskRepository) {

    /**
     * Updates a task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) =
        taskRepository.updateTask(task)
}
