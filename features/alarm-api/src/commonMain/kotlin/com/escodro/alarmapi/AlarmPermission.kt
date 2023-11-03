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
     * Opens the screen to request the Exact alarm permission since it's handled from a different
     * flow than the runtime permissions.
     */
    fun openExactAlarmPermissionScreen()

    /**
     * Opens the app settings screen to user manually grant the runtime permissions.
     */
    fun openAppSettings()
}
