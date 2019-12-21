package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to set a task as uncompleted in the database.
 */
class UncompleteTask(private val taskRepository: TaskRepository) {

    /**
     * Completes the given task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) {
        val updatedTask = updateTaskAsUncompleted(task)
        return taskRepository.updateTask(updatedTask)
    }

    private fun updateTaskAsUncompleted(task: Task) =
        task.copy(completed = false, completedDate = null)
}
