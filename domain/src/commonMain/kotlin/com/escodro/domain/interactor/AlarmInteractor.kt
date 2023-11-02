package com.escodro.domain.interactor

import com.escodro.domain.model.Task

/**
 * Contract to interact with the Alarm layer.
 */
interface AlarmInteractor {

    /**
     * Schedules a new alarm.
     *
     * @param task the alarm id
     * @param timeInMillis the time to the alarm be scheduled
     */
    fun schedule(task: Task, timeInMillis: Long)

    /**
     * Cancels an alarm.
     *
     * @param task the task alarm to be cancelled
     */
    fun cancel(task: Task)
}
