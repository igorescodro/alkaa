package com.escodro.core.extension

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context

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
