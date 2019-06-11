package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.escodro.alarm.TaskReceiver
import com.escodro.core.extension.cancelAlarm
import com.escodro.core.extension.setAlarm
import com.escodro.domain.viewdata.ViewData
import timber.log.Timber

/**
 * Alarm manager to schedule a event based on the due date from a [Task].
 */
class TaskNotificationScheduler(private val context: Context) {

    /**
     * Schedules a task notification based on the due date.
     *
     * @param task task to be scheduled
     */
    fun scheduleTaskAlarm(task: ViewData.Task) {
        val receiverIntent = Intent(context, TaskReceiver::class.java).apply {
            action = TaskReceiver.ALARM_ACTION
            putExtra(TaskReceiver.EXTRA_TASK, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            receiverIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        task.dueDate?.time?.time?.let {
            Timber.d("Scheduling notification for '${task.title}' at '${task.dueDate?.time}'")
            context.setAlarm(it, pendingIntent)
        }
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
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        Timber.d("Canceling notification with id '$taskId'")
        context.cancelAlarm(pendingIntent)
    }
}
