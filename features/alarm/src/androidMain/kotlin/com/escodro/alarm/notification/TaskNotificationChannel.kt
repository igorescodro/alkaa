package com.escodro.alarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.escodro.alarm.extension.getNotificationManager
import com.escodro.resources.MR

/**
 * [NotificationChannel] to send Task notifications in Android O and above.
 */
internal class TaskNotificationChannel(context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(MR.strings.channel_task_name.resourceId)
            val description = context.getString(MR.strings.channel_task_description.resourceId)
            val importance = NotificationManager.IMPORTANCE_HIGH

            NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
                context.getNotificationManager()?.createNotificationChannel(this)
            }
        }
    }

    /**
     * Gets the [TaskNotificationChannel] id.
     *
     * @return the [TaskNotificationChannel] id
     */
    fun getChannelId() = CHANNEL_ID

    companion object {

        const val CHANNEL_ID = "task_notification_channel"
    }
}
