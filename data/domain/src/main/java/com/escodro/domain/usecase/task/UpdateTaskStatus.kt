package com.escodro.domain.usecase.task

import com.escodro.domain.repository.TaskRepository

/**
 * Use case to update a task as completed or uncompleted from the database.
 */
class UpdateTaskStatus(
    private val taskRepository: TaskRepository,
    private val completeTask: CompleteTask,
    private val uncompleteTask: UncompleteTask
) {

    /**
     * Updates the task as completed or uncompleted based on the current state.
     *
     * @param taskId the id from the task to be updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId)
        when (task.completed.not()) {
            true -> completeTask(task)
            false -> uncompleteTask(task)
        }
    }
}
