package com.escodro.domain.interactor

/**
 * Contract to interact with the Alarm layer.
 */
interface AlarmInteractor {

    /**
     * Schedules a new alarm.
     *
     * @param alarmId the alarm id
     * @param timeInMillis the time to the alarm be scheduled
     */
    fun schedule(alarmId: Long, timeInMillis: Long)

    /**
     * Cancels an alarm.
     *
     * @param alarmId the alarm id
     */
    fun cancel(alarmId: Long)
}
