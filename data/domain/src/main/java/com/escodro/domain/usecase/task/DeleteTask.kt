package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to delete a task from the database.
 */
class DeleteTask(private val taskRepository: TaskRepository) {

    /**
     * Deletes a task.
     *
     * @param task the task to be deleted
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) =
        taskRepository.deleteTask(task)
}
