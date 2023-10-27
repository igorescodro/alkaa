package com.escodro.alarm.interactor

import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.domain.interactor.AlarmInteractor
import mu.KotlinLogging

internal class AlarmInteractorImpl(
    private val notificationScheduler: NotificationScheduler,
) : AlarmInteractor {

    private val logger = KotlinLogging.logger {}

    override fun schedule(alarmId: Long, timeInMillis: Long) {
        logger.debug { "schedule - alarmId = $alarmId" }
        notificationScheduler.scheduleTaskNotification(alarmId, timeInMillis)
    }

    override fun cancel(alarmId: Long) {
        logger.debug { "cancel - alarmId = $alarmId" }
        notificationScheduler.cancelTaskNotification(alarmId)
    }
}
