package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval

/**
 * Updates the task as repeating.
 */
interface UpdateTaskAsRepeating {

    /**
     * Updates the task as repeating.
     *
     * @param taskId task id to be updated
     * @param interval repeating alarm interval
     */
    suspend operator fun invoke(taskId: Long, interval: AlarmInterval?)
}
