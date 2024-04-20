package com.escodro.alarm.notification

import platform.Foundation.NSLog
import platform.UserNotifications.UNNotificationResponse

class NotificationActionDelegate {

    fun userNotificationCenter(response: UNNotificationResponse) {
        val taskId =
            response.notification.request.content.userInfo[IosNotificationScheduler.USER_INFO_TASK_ID]

        NSLog("User notification center response: $taskId")
    }
}
