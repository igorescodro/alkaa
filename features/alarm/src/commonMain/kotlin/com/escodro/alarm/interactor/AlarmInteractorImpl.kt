package com.escodro.alarm.interactor

import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import mu.KotlinLogging

internal class AlarmInteractorImpl(
    private val notificationScheduler: NotificationScheduler,
    private val mapper: TaskMapper,
) : AlarmInteractor {

    private val logger = KotlinLogging.logger {}

    override fun schedule(task: Task, timeInMillis: Long) {
        logger.debug { "schedule - alarmId = $task" }
        val alarmTask = mapper.fromDomain(task)
        notificationScheduler.scheduleTaskNotification(alarmTask, timeInMillis)
    }

    override fun cancel(task: Task) {
        logger.debug { "cancel - alarmId = $task" }
        val alarmTask = mapper.fromDomain(task)
        notificationScheduler.cancelTaskNotification(alarmTask)
    }
}
