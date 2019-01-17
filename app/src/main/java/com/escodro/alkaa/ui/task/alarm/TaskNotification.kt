package com.escodro.alkaa.ui.task.alarm

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.getNotificationManager
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.TaskNotificationChannel
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
            setSmallIcon(R.drawable.ic_alarm)
            setContentTitle(context.getString(R.string.app_name))
            setContentText(task.title)
            setContentIntent(buildPendingIntent(task))
            setAutoCancel(true)
        }.build()

    private fun buildPendingIntent(task: Task): PendingIntent {
        val arguments = Bundle()
        arguments.putParcelable(ARGUMENT_TASK, task)

        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskDetailFragment)
            .setArguments(arguments)
            .createPendingIntent()
    }

    companion object {

        /**
         * Once it is not possible yet to use _Navigation Safe Args_ creating a
         * [NavDeepLinkBuilder], the argument name must be passed hardcoded. This value must match
         * with the argument in _nav.graph.xml_.
         */
        private const val ARGUMENT_TASK = "task"
    }
}
