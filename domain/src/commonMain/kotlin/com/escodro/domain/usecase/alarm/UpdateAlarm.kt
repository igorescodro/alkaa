package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task

/**
 * Use case to update the alarm of a task.
 */
interface UpdateAlarm {

    /**
     * Updates the alarm of a task.
     *
     * @param task task to be updated
     */
    suspend operator fun invoke(task: Task)
}
