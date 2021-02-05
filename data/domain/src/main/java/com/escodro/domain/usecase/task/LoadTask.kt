package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task

interface LoadTask {
    /**
     * Gets a task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long): Task
}
