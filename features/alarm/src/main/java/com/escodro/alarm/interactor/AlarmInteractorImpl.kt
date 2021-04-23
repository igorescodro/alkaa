package com.escodro.alarm.interactor

import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.domain.interactor.AlarmInteractor
import mu.KLogging

internal class AlarmInteractorImpl(private val alarmManager: TaskNotificationScheduler) :
    AlarmInteractor {

    override fun schedule(alarmId: Long, timeInMillis: Long) {
        logger.debug("schedule - alarmId = $alarmId")
        alarmManager.scheduleTaskAlarm(alarmId, timeInMillis)
    }

    override fun cancel(alarmId: Long) {
        logger.debug("cancel - alarmId = $alarmId")
        alarmManager.cancelTaskAlarm(alarmId)
    }

    companion object : KLogging()
}
