package com.escodro.alkaa.ui.task.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.getNotificationManager
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import timber.log.Timber

/**
 * Handles the notification related to the [Task] reminders.
 */
class TaskNotification(
    private val context: Context,
    private val channel: TaskNotificationChannel
) {

    /**
     * Shows the [TaskNotification] based on the given [Task].
     *
     * @param task the task to be shown in the notification
     */
    fun show(task: Task) {
        Timber.d("Showing notification for '${task.title}'")
        val notification = buildNotification(task)
        context.getNotificationManager()?.notify(task.id.toInt(), notification)
    }

    private fun buildNotification(task: Task) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.drawable.ic_bookmark_check)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(task.title)
            setContentIntent(buildPendingIntent(task))
            setAutoCancel(true)
            addAction(getCompleteAction(task))
            addAction(getSnoozeAction(task))
        }.build()

    private fun buildPendingIntent(task: Task): PendingIntent {
        val arguments = Bundle()
        arguments.putLong(ARGUMENT_TASK, task.id)

        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskDetailFragment)
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

    private fun getIntent(task: Task, intentAction: String, requestCode: Int): PendingIntent {
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
