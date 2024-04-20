package com.escodro.alarm.notification

import com.escodro.alarm.model.Task
import com.escodro.resources.MR
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
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
import platform.UserNotifications.UNNotificationAction
import platform.UserNotifications.UNNotificationActionOptionNone
import platform.UserNotifications.UNNotificationCategory
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

internal class IosNotificationScheduler : NotificationScheduler {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    init {
        registerCategories()
    }

    private fun registerCategories() {
        val doneAction = UNNotificationAction.actionWithIdentifier(
            identifier = ACTION_IDENTIFIER_DONE,
            title = StringDesc.Resource(MR.strings.notification_action_completed).localized(),
            options = UNNotificationActionOptionNone,
        )

        val category = UNNotificationCategory.categoryWithIdentifier(
            identifier = CATEGORY_IDENTIFIER_TASK,
            actions = listOf(doneAction),
            intentIdentifiers = emptyList<String>(),
            options = UNNotificationActionOptionNone,
        )

        notificationCenter.setNotificationCategories(setOf(category))
    }

    override fun scheduleTaskNotification(task: Task, timeInMillis: Long) {
        val content = UNMutableNotificationContent()
        content.setBody(task.title)
        content.setCategoryIdentifier(CATEGORY_IDENTIFIER_TASK)
        content.setUserInfo(mapOf(USER_INFO_TASK_ID to task.id))

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
            identifier = task.id.toString(),
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
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(listOf(task.id.toString()))
    }

    override fun updateTaskNotification(task: Task) {
        NSLog("Updating notification with id '${task.title}'")
        val time = task.dueDate?.toInstant(
            TimeZone.currentSystemDefault(),
        )?.toEpochMilliseconds() ?: return

        scheduleTaskNotification(task, time)
    }

    companion object {

        /**
         * Identifier for the task category actions.
         */
        const val CATEGORY_IDENTIFIER_TASK = "task_actions"

        /**
         * Identifier for the done action.
         */
        const val ACTION_IDENTIFIER_DONE = "done_action"

        /**
         * Key to store the task id in the notification content.
         */
        const val USER_INFO_TASK_ID = "task_id"
    }
}
