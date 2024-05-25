package com.escodro.alarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.escodro.alarm.extension.getNotificationManager
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.resources.Res
import com.escodro.resources.channel_task_description
import com.escodro.resources.channel_task_name
import org.jetbrains.compose.resources.getString

/**
 * [NotificationChannel] to send Task notifications in Android O and above.
 */
internal class TaskNotificationChannel(context: Context, appCoroutineScope: AppCoroutineScope) {

    init {
        appCoroutineScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = getString(Res.string.channel_task_name)
                val description = getString(Res.string.channel_task_description)
                val importance = NotificationManager.IMPORTANCE_HIGH

                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    this.description = description
                    context.getNotificationManager()?.createNotificationChannel(this)
                }
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
