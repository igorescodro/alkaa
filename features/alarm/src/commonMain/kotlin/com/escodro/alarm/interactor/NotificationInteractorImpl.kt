package com.escodro.alarm.interactor

import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import mu.KotlinLogging

internal class NotificationInteractorImpl(
    private val taskNotification: TaskNotification,
    private val taskMapper: TaskMapper,
) : NotificationInteractor {

    private val logger = KotlinLogging.logger {}

    override fun show(task: Task) {
        logger.debug { "show - alarmId = ${task.id}" }
        if (task.isRepeating) {
            taskNotification.showRepeating(taskMapper.fromDomain(task))
        } else {
            taskNotification.show(taskMapper.fromDomain(task))
        }
    }

    override fun dismiss(task: Task) {
        logger.debug { "dismiss - alarmId = $task" }
        taskNotification.dismiss(task.id)
    }
}
