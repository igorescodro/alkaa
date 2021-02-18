package com.escodro.domain.usecase.task

/**
 * Use case to update a task description.
 */
interface UpdateTaskDescription {

    /**
     * Updates a task description.
     *
     * @param taskId the task id to be updated
     * @param description the description to be set
     */
    suspend operator fun invoke(taskId: Long, description: String)
}
