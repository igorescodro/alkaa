package com.escodro.alkaa.common.extension

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.AlarmManagerCompat

/**
 * Sets a alarm using [AlarmManagerCompat] to be triggered based on the given parameter.
 *
 * @param triggerAtMillis time in milliseconds that the alarm should go off, using the
 * appropriate clock (depending on the alarm type).
 * @param operation Action to perform when the alarm goes off
 * @param type type to define how the alarm will behave
 */
fun Context.setAlarm(
    triggerAtMillis: Long,
    operation: PendingIntent,
    type: Int = AlarmManager.RTC_WAKEUP
) {
    val manager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    manager?.let { it ->
        AlarmManagerCompat.setAndAllowWhileIdle(it, type, triggerAtMillis, operation)
    }
}

/**
 * Gets the [NotificationManager] system service.
 *
 * @return the [NotificationManager] system service
 */
fun Context.getNotificationManager() =
    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
