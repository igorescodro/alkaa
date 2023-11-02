package com.escodro.alarm.notification

import com.escodro.alarm.model.Task

/**
 * Interface to schedule and cancel notifications on each platform.
 */
internal interface NotificationScheduler {

    /**
     * Schedules a notification with task content to be shown at the given time.
     *
     * @param task the task information to be shown
     * @param timeInMillis the time in milliseconds to show the notification
     */
    fun scheduleTaskNotification(task: Task, timeInMillis: Long)

    /**
     * Cancels the notification with the given task id.
     *
     * @param task the task id to be canceled
     */
    fun cancelTaskNotification(task: Task)
}
