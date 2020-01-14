package com.escodro.alarm.interactor

import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.domain.interactor.AlarmInteractor
import timber.log.Timber

internal class AlarmInteractorImpl(private val alarmManager: TaskNotificationScheduler) :
    AlarmInteractor {

    override fun schedule(alarmId: Long, timeInMillis: Long) {
        Timber.d("schedule - alarmId = $alarmId")
        alarmManager.scheduleTaskAlarm(alarmId, timeInMillis)
    }

    override fun cancel(alarmId: Long) {
        Timber.d("cancel - alarmId = $alarmId")
        alarmManager.cancelTaskAlarm(alarmId)
    }
}
