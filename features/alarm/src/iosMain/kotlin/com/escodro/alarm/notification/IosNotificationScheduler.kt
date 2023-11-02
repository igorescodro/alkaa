package com.escodro.alarm.notification

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSLog
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

internal class IosNotificationScheduler : NotificationScheduler {

    override fun scheduleTaskNotification(taskId: Long, timeInMillis: Long) {
        val content = UNMutableNotificationContent()
        content.setTitle("Task Title")
        content.setBody("Task Description")

        val nsDate = NSDate.dateWithTimeIntervalSince1970(timeInMillis / 1000.0)
        val dateComponents = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay
                or NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = nsDate,
        )

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = dateComponents,
            repeats = false,
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            taskId.toString(),
            content = content,
            trigger = trigger,
        )

        NSLog("Scheduling notification for '$taskId' at '$timeInMillis'")
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.addNotificationRequest(request) { error ->
            if (error != null) {
                NSLog("Error scheduling notification: $error")
            }
        }
    }

    override fun cancelTaskNotification(taskId: Long) {
        NSLog("Canceling notification with id '$taskId'")
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(taskId.toString()))
    }
}
