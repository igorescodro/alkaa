package com.escodro.alarm.interactor

import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import timber.log.Timber
import javax.inject.Inject

internal class NotificationInteractorImpl @Inject constructor(
    private val taskNotification: TaskNotification,
    private val taskMapper: TaskMapper
) : NotificationInteractor {

    override fun show(task: Task) {
        Timber.d("show - alarmId = ${task.id}")
        if (task.isRepeating) {
            taskNotification.showRepeating(taskMapper.fromDomain(task))
        } else {
            taskNotification.show(taskMapper.fromDomain(task))
        }
    }

    override fun dismiss(notificationId: Long) {
        Timber.d("dismiss - alarmId = $notificationId")
        taskNotification.dismiss(notificationId)
    }
}
