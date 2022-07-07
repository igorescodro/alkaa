package com.escodro.alarmapi

/**
 * Handles all the alarm-related permission verifications.
 */
interface AlarmPermission {

    /**
     * Verifies if the permission [android.Manifest.permission.SCHEDULE_EXACT_ALARM] is granted.
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    fun hasExactAlarmPermission(): Boolean

    /**
     * Verifies if the [android.Manifest.permission.POST_NOTIFICATIONS] is granted.
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    fun hasNotificationPermission(): Boolean
}
