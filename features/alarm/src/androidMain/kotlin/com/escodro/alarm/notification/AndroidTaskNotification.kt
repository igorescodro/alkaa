package com.escodro.alarm.notification

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.escodro.alarm.R
import com.escodro.alarm.extension.getNotificationManager
import com.escodro.alarm.model.Task
import com.escodro.alarm.receiver.TaskNotificationReceiver
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.navigation.AndroidDestinations
import com.escodro.resources.Res
import com.escodro.resources.content_app_name
import com.escodro.resources.notification_action_completed
import com.escodro.resources.notification_action_snooze
import logcat.logcat
import org.jetbrains.compose.resources.getString

/**
 * Handles the notification related to the Task reminders.
 */
internal class AndroidTaskNotification(
    private val context: Context,
    private val appCoroutineScope: AppCoroutineScope,
    private val channel: TaskNotificationChannel,
) : TaskNotification {

    /**
     * Shows the [AndroidTaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    override fun show(task: Task) {
        appCoroutineScope.launch {
            logcat { "Showing notification for '${task.title}'" }
            val builder = buildNotification(task)
            builder.addAction(getCompleteAction(task))

            context.getNotificationManager()?.let { notificationManager ->
                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(task.id.toInt(), builder.build())
                }
            }
        }
    }

    /**
     * Shows the repeating [AndroidTaskNotification] based on the given Task.
     *
     * @param task the task to be shown in the notification
     */
    override fun showRepeating(task: Task) {
        appCoroutineScope.launch {
            logcat { "Showing repeating notification for '${task.title}'" }
            val builder = buildNotification(task)

            context.getNotificationManager()?.let { notificationManager ->
                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(task.id.toInt(), builder.build())
                }
            }
        }
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

    private suspend fun buildNotification(task: Task): NotificationCompat.Builder {
        val title = getString(Res.string.content_app_name)

        return NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.drawable.ic_bookmark)
            setContentTitle(title)
            setContentText(task.title)
            setContentIntent(buildPendingIntent(task.id))
            setAutoCancel(true)
            addAction(getSnoozeAction(task))
        }
    }

    private fun buildPendingIntent(taskId: Long): PendingIntent =
        TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(AndroidDestinations.taskDetail(taskId))
            getPendingIntent(
                REQUEST_CODE_OPEN_TASK,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

    private suspend fun getCompleteAction(task: Task): NotificationCompat.Action {
        val actionTitle = getString(Res.string.notification_action_completed)
        val intent =
            getIntent(task, TaskNotificationReceiver.COMPLETE_ACTION, REQUEST_CODE_ACTION_COMPLETE)
        return NotificationCompat.Action(ACTION_NO_ICON, actionTitle, intent)
    }

    private suspend fun getSnoozeAction(task: Task): NotificationCompat.Action {
        val actionTitle = getString(Res.string.notification_action_snooze)
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
