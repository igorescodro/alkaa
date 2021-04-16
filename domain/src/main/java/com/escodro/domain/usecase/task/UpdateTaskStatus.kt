package com.escodro.domain.usecase.task

/**
 * Use case to update a task as completed or uncompleted from the database.
 */
interface UpdateTaskStatus {

    /**
     * Updates the task as completed or uncompleted based on the current state.
     *
     * @param taskId the id from the task to be updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long)
}
