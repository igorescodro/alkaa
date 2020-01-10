package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor

/**
 * Use case to cancel an alarm.
 */
class CancelAlarm(private val alarmInteractor: AlarmInteractor) {

    /**
     * Cancels an alarm.
     *
     * @param alarmId the alarm id
     */
    operator fun invoke(alarmId: Long) =
        alarmInteractor.cancel(alarmId)
}
