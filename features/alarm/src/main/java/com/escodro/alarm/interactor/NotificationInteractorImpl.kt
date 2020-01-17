package com.escodro.alarm.interactor

import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import timber.log.Timber

internal class NotificationInteractorImpl(
    private val taskNotification: TaskNotification,
    private val taskMapper: TaskMapper
) : NotificationInteractor {

    override fun show(task: Task) {
        Timber.d("show - alarmId = ${task.id}")
        taskNotification.show(taskMapper.fromDomain(task))
    }

    override fun dismiss(notificationId: Long) {
        Timber.d("dismiss - alarmId = $notificationId")
        taskNotification.dismiss(notificationId)
    }
}
