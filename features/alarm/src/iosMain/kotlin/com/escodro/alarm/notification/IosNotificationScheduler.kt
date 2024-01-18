package com.escodro.alarm.notification

import com.escodro.alarm.model.Task
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

    override fun scheduleTaskNotification(task: Task, timeInMillis: Long) {
        val content = UNMutableNotificationContent()
        content.setBody(task.title)

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
            task.id.toString(),
            content = content,
            trigger = trigger,
        )

        NSLog("Scheduling notification for '${task.title}' at '$timeInMillis'")
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.addNotificationRequest(request) { error ->
            if (error != null) {
                NSLog("Error scheduling notification: $error")
            }
        }
    }

    override fun cancelTaskNotification(task: Task) {
        NSLog("Canceling notification with id '${task.title}'")
        val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(task.id.toString()))
    }
}
