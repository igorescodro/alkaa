package com.escodro.domain.usecase.task

import com.escodro.domain.repository.TaskRepository

/**
 * Use case to get a task from the database.
 */
class GetTask(private val taskRepository: TaskRepository) {

    /**
     * Gets a task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long) =
        taskRepository.findTaskFlowById(taskId)
}
