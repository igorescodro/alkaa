package com.escodro.alarm.notification

import com.escodro.alarm.model.Task
import platform.UserNotifications.UNUserNotificationCenter

internal class IosTaskNotification : TaskNotification {

    override fun show(task: Task) {
        // Do nothing - in iOS, the notification scheduler is responsible for showing the notification
    }

    override fun showRepeating(task: Task) {
        // Do nothing - For now the iOS notifications won't have action
    }

    override fun dismiss(taskId: Long) {
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.removeDeliveredNotificationsWithIdentifiers(listOf(taskId.toString()))
    }
}
