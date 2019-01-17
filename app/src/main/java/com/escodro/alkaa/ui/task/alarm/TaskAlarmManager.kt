package com.escodro.alkaa.ui.task.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.escodro.alkaa.common.extension.cancelAlarm
import com.escodro.alkaa.common.extension.marshallParcelable
import com.escodro.alkaa.common.extension.setAlarm
import com.escodro.alkaa.data.local.model.Task
import timber.log.Timber

/**
 * Alarm manager to schedule a event based on the due date from a [Task].
 */
class TaskAlarmManager(private val context: Context) {

    /**
     * Schedules a task notification based on the due date.
     *
     * @param task task to be scheduled
     */
    fun scheduleTaskAlarm(task: Task) {
        val byte = marshallParcelable(task)
        val receiverIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
            action = ALARM_ACTION
            putExtra(EXTRA_TASK, byte)
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
        val receiverIntent = Intent(context, TaskAlarmReceiver::class.java)
        receiverIntent.action = ALARM_ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        Timber.d("Canceling notification with id '$taskId'")
        context.cancelAlarm(pendingIntent)
    }

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"
    }
}
