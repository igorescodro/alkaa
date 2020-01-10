package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor

/**
 * Use case to schedule a new alarm.
 */
class ScheduleAlarm(private val alarmInteractor: AlarmInteractor) {

    /**
     * Schedules a new alarm.
     *
     * @param alarmId the alarm id
     * @param timeInMillis the time to the alarm be scheduled
     */
    operator fun invoke(alarmId: Long, timeInMillis: Long?) =
        alarmInteractor.schedule(alarmId, timeInMillis)
}
