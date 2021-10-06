package com.escodro.alarm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.escodro.alarm.R
import com.escodro.core.extension.getNotificationManager
import mu.KLogging

/**
 * Handles the notification related to the Alarm Permission.
 */
internal class PermissionNotification(
    private val context: Context,
    private val channel: TaskNotificationChannel
) {

    /**
     * Shows the [PermissionNotification] when the permission is removed.
     */
    fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val builder = buildNotification()
            context.getNotificationManager()?.notify(NOTIFICATION_ID, builder.build())
            logger.debug("Showing notification for alarm permission")
        } else {
            logger.debug("Notification not shown - Android version is below 12")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun buildNotification() =
        NotificationCompat.Builder(context, channel.getChannelId()).apply {
            setSmallIcon(R.drawable.ic_bookmark_check)
            setContentTitle(context.getString(R.string.alarm_permission_notification_title))
            setContentText(context.getString(R.string.alarm_permission_notification_content))
            setContentIntent(buildPendingIntent())
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.alarm_permission_notification_big_text))
                )
            setAutoCancel(true)
        }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
        }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_OPEN_PERMISSION,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private companion object : KLogging() {

        private const val NOTIFICATION_ID = 97_112

        private const val REQUEST_CODE_OPEN_PERMISSION = 1_121_111
    }
}
