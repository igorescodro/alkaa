package com.escodro.alarm.notification

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.escodro.alarm.model.Task
import com.escodro.alarm.receiver.TaskNotificationReceiver
import com.escodro.core.extension.getNotificationManager
import com.escodro.resources.MR
import logcat.logcat

/**
 * Handles the notification related to the Task reminders.
 */
internal class AndroidTaskNotification(
    private val context: Context,
    private val channel: TaskNotificationChannel,
    // private val alarmPermission: AlarmPermission, TODO
) : TaskNotification {

    /**
     * Shows the [AndroidTaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    override fun show(task: Task) {
        logcat { "Showing notification for '${task.title}'" }
        val builder = buildNotification(task)
        builder.addAction(getCompleteAction(task))

        // if (!alarmPermission.hasNotificationPermission()) {
        //     logcat { "Permission not granted - ignoring alarm" }
        //     return
        // }
        context.getNotificationManager()?.notify(task.id.toInt(), builder.build())
    }

    /**
     * Shows the repeating [AndroidTaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    override fun showRepeating(task: Task) {
        logcat { "Showing repeating notification for '${task.title}'" }
        val builder = buildNotification(task)

        // if (!alarmPermission.hasNotificationPermission()) {
        //     logcat { "Permission not granted - ignoring alarm" }
        //     return
        // }
        context.getNotificationManager()?.notify(task.id.toInt(), builder.build())
    }

    /**
     * Dismiss the [AndroidTaskNotification] based on the given id.
     *
     * @param taskId the notification id to be dismissed
     */
    override fun dismiss(taskId: Long) {
        logcat { "Dismissing notification id '$taskId'" }
        context.getNotificationManager()?.cancel(taskId.toInt())
    }

    private fun buildNotification(task: Task) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(MR.images.ic_bookmark.drawableResId)
            setContentTitle(MR.strings.content_app_name.getString(context))
            setContentText(task.title)
            setContentIntent(buildPendingIntent())
            setAutoCancel(true)
            addAction(getSnoozeAction(task))
        }

    private fun buildPendingIntent(): PendingIntent {
        val openTaskIntent = Intent(
            Intent.ACTION_VIEW,
            // DestinationDeepLink.getTaskDetailUri(task.id), TODO
        )

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(openTaskIntent)
            getPendingIntent(
                REQUEST_CODE_OPEN_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }

    private fun getCompleteAction(task: Task): NotificationCompat.Action {
        val actionTitle = context.getString(MR.strings.notification_action_completed.resourceId)
        val intent =
            getIntent(task, TaskNotificationReceiver.COMPLETE_ACTION, REQUEST_CODE_ACTION_COMPLETE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getSnoozeAction(task: Task): NotificationCompat.Action {
        val actionTitle = context.getString(MR.strings.notification_action_snooze.resourceId)
        val intent =
            getIntent(task, TaskNotificationReceiver.SNOOZE_ACTION, REQUEST_CODE_ACTION_SNOOZE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getIntent(
        task: Task,
        intentAction: String,
        requestCode: Int,
    ): PendingIntent {
        val receiverIntent = Intent(context, TaskNotificationReceiver::class.java).apply {
            action = intentAction
            putExtra(TaskNotificationReceiver.EXTRA_TASK, task.id)
        }

        return PendingIntent
            .getBroadcast(
                context,
                requestCode,
                receiverIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
    }

    companion object {

        private const val REQUEST_CODE_OPEN_TASK = 1_121_111

        private const val REQUEST_CODE_ACTION_COMPLETE = 1_234

        private const val REQUEST_CODE_ACTION_SNOOZE = 4_321

        private const val ACTION_NO_ICON = 0
    }
}
