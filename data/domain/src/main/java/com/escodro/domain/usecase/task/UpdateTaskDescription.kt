package com.escodro.domain.usecase.task

/**
 * Use case to update a task description.
 */
interface UpdateTaskDescription {

    /**
     * Updates a task description.
     *
     * @param taskId the task id to be updated
     * @param taskId the title to be set
     */
    suspend operator fun invoke(taskId: Long, description: String)
}
