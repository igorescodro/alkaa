package com.escodro.alarm.interactor

import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import mu.KLogging

internal class NotificationInteractorImpl(
    private val taskNotification: TaskNotification,
    private val taskMapper: TaskMapper
) : NotificationInteractor {

    override fun show(task: Task) {
        logger.debug("show - alarmId = ${task.id}")
        if (task.isRepeating) {
            taskNotification.showRepeating(taskMapper.fromDomain(task))
        } else {
            taskNotification.show(taskMapper.fromDomain(task))
        }
    }

    override fun dismiss(notificationId: Long) {
        logger.debug("dismiss - alarmId = $notificationId")
        taskNotification.dismiss(notificationId)
    }

    companion object : KLogging()
}
