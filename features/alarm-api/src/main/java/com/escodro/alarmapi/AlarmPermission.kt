package com.escodro.alarmapi

/**
 * Handles [android.Manifest.permission.SCHEDULE_EXACT_ALARM] verification.
 */
interface AlarmPermission {

    /**
     * Verifies if the permission [android.Manifest.permission.SCHEDULE_EXACT_ALARM] is granted.
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    fun hasExactAlarmPermission(): Boolean
}
