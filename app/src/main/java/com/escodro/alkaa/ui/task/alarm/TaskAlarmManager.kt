package com.escodro.alkaa.ui.task.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.escodro.alkaa.common.extension.cancelAlarm
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
        val receiverIntent = Intent(context, TaskAlarmReceiver::class.java)
        receiverIntent.putExtra(EXTRA_TASK_ID, task.id)
        receiverIntent.putExtra(EXTRA_TASK_DESCRIPTION, task.description)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        task.dueDate?.time?.time?.let {
            Timber.d("Scheduling notification for '${task.description}' at '${task.dueDate?.time}'")
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

        const val EXTRA_TASK_ID = "extra_task_id"

        const val EXTRA_TASK_DESCRIPTION = "extra_task_description"
    }
}
