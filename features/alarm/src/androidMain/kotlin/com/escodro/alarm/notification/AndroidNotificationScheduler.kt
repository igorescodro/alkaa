package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.escodro.alarm.extension.cancelAlarm
import com.escodro.alarm.extension.setExactAlarm
import com.escodro.alarm.model.Task
import com.escodro.alarm.receiver.TaskNotificationReceiver
import logcat.logcat

/**
 * Alarm manager to schedule a event based on the due date from a Task.
 */
internal class AndroidNotificationScheduler(private val context: Context) : NotificationScheduler {

    override fun scheduleTaskNotification(task: Task, timeInMillis: Long) {
        val receiverIntent = Intent(context, TaskNotificationReceiver::class.java).apply {
            action = TaskNotificationReceiver.ALARM_ACTION
            putExtra(TaskNotificationReceiver.EXTRA_TASK, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            receiverIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        logcat { "Scheduling notification for '${task.title}' at '$timeInMillis'" }
        context.setExactAlarm(timeInMillis, pendingIntent)
    }

    override fun cancelTaskNotification(task: Task) {
        val receiverIntent = Intent(context, TaskNotificationReceiver::class.java)
        receiverIntent.action = TaskNotificationReceiver.ALARM_ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        logcat { "Canceling notification with id '${task.title}'" }
        context.cancelAlarm(pendingIntent)
    }
}
