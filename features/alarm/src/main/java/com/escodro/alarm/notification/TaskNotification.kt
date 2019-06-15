package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.escodro.alarm.R
import com.escodro.alarm.TaskReceiver
import com.escodro.core.extension.getNotificationManager
import com.escodro.domain.viewdata.ViewData
import timber.log.Timber

/**
 * Handles the notification related to the [com.escodro.domain.viewdata.ViewData.Task] reminders.
 */
internal class TaskNotification(
    private val context: Context,
    private val channel: TaskNotificationChannel
) {

    /**
     * Shows the [TaskNotification] based on the given [com.escodro.domain.viewdata.ViewData.Task].
     *
     * @param task the task to be shown in the notification
     */
    fun show(task: ViewData.Task) {
        Timber.d("Showing notification for '${task.title}'")
        val notification = buildNotification(task)
        context.getNotificationManager()?.notify(task.id.toInt(), notification)
    }

    private fun buildNotification(task: ViewData.Task) =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.drawable.ic_bookmark_check)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(task.title)
            setContentIntent(buildPendingIntent(task))
            setAutoCancel(true)
            addAction(getCompleteAction(task))
            addAction(getSnoozeAction(task))
        }.build()

    private fun buildPendingIntent(task: ViewData.Task): PendingIntent {
        val arguments = Bundle()
        arguments.putLong(ARGUMENT_TASK, task.id)

        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskDetailFragment)
            .setArguments(arguments)
            .createPendingIntent()
    }

    private fun getCompleteAction(task: ViewData.Task): NotificationCompat.Action {
        val actionTitle = context.getString(R.string.notification_action_completed)
        val intent = getIntent(task, TaskReceiver.COMPLETE_ACTION, REQUEST_CODE_ACTION_COMPLETE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getSnoozeAction(task: ViewData.Task): NotificationCompat.Action {
        val actionTitle = context.getString(R.string.notification_action_snooze)
        val intent = getIntent(task, TaskReceiver.SNOOZE_ACTION, REQUEST_CODE_ACTION_SNOOZE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private fun getIntent(
        task: ViewData.Task,
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
