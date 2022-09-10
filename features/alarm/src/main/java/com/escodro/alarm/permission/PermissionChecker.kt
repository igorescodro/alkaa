package com.escodro.alarm.permission

/**
 * Represents the Android permission checker.
 */
internal interface PermissionChecker {

    /**
     * Checks if the given permission was granted by the user.
     *
     * @param permission the Android permission
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    fun checkPermission(permission: String): Boolean

    /**
     * Checks if the special schedule exact alarms permission was granted by the user.
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    fun canScheduleExactAlarms(): Boolean
}
