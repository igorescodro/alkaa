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
 * @param operation action to perform when the alarm goes off
 * @param type type to define how the alarm will behave
 */
fun Context.setAlarm(
    triggerAtMillis: Long,
    operation: PendingIntent,
    type: Int = AlarmManager.RTC_WAKEUP
) {
    val manager = getAlarmManager()
    manager?.let { AlarmManagerCompat.setAndAllowWhileIdle(it, type, triggerAtMillis, operation) }
}

/**
 * Cancels a alarm set on [AlarmManager], based on the given [PendingIntent].
 *
 * @param operation action to be canceld
 */
fun Context.cancelAlarm(operation: PendingIntent) {
    val manager = getAlarmManager()
    manager?.let { manager.cancel(operation) }
}

/**
 * Gets the [NotificationManager] system service.
 *
 * @return the [NotificationManager] system service
 */
fun Context.getNotificationManager() =
    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

/**
 * Gets the [AlarmManager] system service.
 *
 * @return the [AlarmManager] system service
 */
fun Context.getAlarmManager() =
    getSystemService(Context.ALARM_SERVICE) as? AlarmManager
