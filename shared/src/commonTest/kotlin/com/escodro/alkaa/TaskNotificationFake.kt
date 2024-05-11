package com.escodro.alkaa

import com.escodro.alarm.model.Task
import com.escodro.alarm.notification.TaskNotification

internal class TaskNotificationFake : TaskNotification {
    override fun show(task: Task) {
        // Do nothing
    }

    override fun showRepeating(task: Task) {
        // Do nothing
    }

    override fun dismiss(taskId: Long) {
        // Do nothing
    }
}
