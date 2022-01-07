package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.escodro.alarm.TaskReceiver
import com.escodro.core.extension.cancelAlarm
import com.escodro.core.extension.setExactAlarm
import logcat.logcat

/**
 * Alarm manager to schedule a event based on the due date from a Task.
 */
internal class TaskNotificationScheduler(private val context: Context) {

    /**
     * Schedules a task notification based on the due date.
     *
     * @param taskId task id to be scheduled
     * @param timeInMillis the time to the alarm be scheduled
     */
    fun scheduleTaskAlarm(taskId: Long, timeInMillis: Long) {
        val receiverIntent = Intent(context, TaskReceiver::class.java).apply {
            action = TaskReceiver.ALARM_ACTION
            putExtra(TaskReceiver.EXTRA_TASK, taskId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            receiverIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        logcat { "Scheduling notification for '$taskId' at '$timeInMillis'" }
        context.setExactAlarm(timeInMillis, pendingIntent)
    }

    /**
     * Cancels a task notification based on the task id.
     *
     * @param taskId task id to be canceled
     */
    fun cancelTaskAlarm(taskId: Long) {
        val receiverIntent = Intent(context, TaskReceiver::class.java)
        receiverIntent.action = TaskReceiver.ALARM_ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        logcat { "Canceling notification with id '$taskId'" }
        context.cancelAlarm(pendingIntent)
    }
}
