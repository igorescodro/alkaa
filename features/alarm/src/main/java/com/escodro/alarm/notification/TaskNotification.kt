package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.escodro.alarm.R
import com.escodro.alarm.TaskReceiver
import com.escodro.alarm.model.Task
import com.escodro.core.extension.getNotificationManager
import timber.log.Timber

/**
 * Handles the notification related to the Task reminders.
 */
internal class TaskNotification(
    private val context: Context,
    private val channel: TaskNotificationChannel
) {

    /**
     * Shows the [TaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    fun show(task: Task) {
        Timber.d("Showing notification for '${task.title}'")
        val builder = buildNotification(task)
        builder.addAction(getCompleteAction(task))
        context.getNotificationManager()?.notify(task.id.toInt(), builder.build())
    }

    /**
     * Shows the repeating [TaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    fun showRepeating(task: Task) {
        Timber.d("Showing repeating notification for '${task.title}'")
        val builder = buildNotification(task)
        context.getNotificationManager()?.notify(task.id.toInt(), builder.build())
    }

    /**
     * Dismiss the [TaskNotification] based on the given id.
     *
     * @param notificationId the notification id to be dismissed
     */
    fun dismiss(notificationId: Long) {
        Timber.d("Dismissing notification id '$notificationId'")
        context.getNotificationManager()?.cancel(notificationId.toInt())
    }

    private fun buildNotification(task: Task) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.drawable.ic_bookmark_check)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(task.title)
            setContentIntent(buildPendingIntent(task))
            setAutoCancel(true)
            addAction(getSnoozeAction(task))
        }

    private fun buildPendingIntent(task: Task): PendingIntent {
        val arguments = Bundle()
        arguments.putLong(ARGUMENT_TASK, task.id)

        return NavDeepLinkBuilder(context)
            .setArguments(arguments)
            .createPendingIntent()
    }

    private fun getCompleteAction(task: Task): NotificationCompat.Action {
        val actionTitle = context.getString(R.string.notification_action_completed)
        val intent = getIntent(task, TaskReceiver.COMPLETE_ACTION, REQUEST_CODE_ACTION_COMPLETE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getSnoozeAction(task: Task): NotificationCompat.Action {
        val actionTitle = context.getString(R.string.notification_action_snooze)
        val intent = getIntent(task, TaskReceiver.SNOOZE_ACTION, REQUEST_CODE_ACTION_SNOOZE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getIntent(
        task: Task,
        intentAction: String,
        requestCode: Int
    ): PendingIntent {
        val receiverIntent = Intent(context, TaskReceiver::class.java).apply {
            action = intentAction
            putExtra(TaskReceiver.EXTRA_TASK, task.id)
        }

        return PendingIntent
            .getBroadcast(context, requestCode, receiverIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    companion object {

        /**
         * Once it is not possible yet to use _Navigation Safe Args_ creating a
         * [NavDeepLinkBuilder], the argument name must be passed hardcoded. This value must match
         * with the argument in _nav.graph.xml_.
         */
        private const val ARGUMENT_TASK = "taskId"

        private const val REQUEST_CODE_ACTION_COMPLETE = 1_234

        private const val REQUEST_CODE_ACTION_SNOOZE = 4_321

        private const val ACTION_NO_ICON = 0
    }
}
