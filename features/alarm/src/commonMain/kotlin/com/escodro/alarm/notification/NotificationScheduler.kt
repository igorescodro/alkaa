package com.escodro.alarm.notification

/**
 * Interface to schedule and cancel notifications on each platform.
 */
internal interface NotificationScheduler {

    /**
     * Schedules a notification with task content to be shown at the given time.
     *
     * @param taskId the task id to be shown
     * @param timeInMillis the time in milliseconds to show the notification
     */
    fun scheduleTaskNotification(taskId: Long, timeInMillis: Long)

    /**
     * Cancels the notification with the given task id.
     *
     * @param taskId the task id to be canceled
     */
    fun cancelTaskNotification(taskId: Long)
}
