package com.escodro.alkaa.fake

import com.escodro.alarm.model.Task
import com.escodro.alarm.notification.NotificationScheduler

internal class NotificationSchedulerFake : NotificationScheduler {
    override fun scheduleTaskNotification(task: Task, timeInMillis: Long) {
        // Do nothing
    }

    override fun cancelTaskNotification(task: Task) {
        // Do nothing
    }

    override fun updateTaskNotification(task: Task) {
        // Do nothing
    }
}
