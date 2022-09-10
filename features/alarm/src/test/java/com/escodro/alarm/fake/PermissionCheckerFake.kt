package com.escodro.alarm.fake

import com.escodro.alarm.permission.PermissionChecker

internal class PermissionCheckerFake : PermissionChecker {

    var hasPermission: Boolean = false

    var hasExactAlarmsPermission: Boolean = false

    override fun checkPermission(permission: String): Boolean =
        hasPermission

    override fun canScheduleExactAlarms(): Boolean =
        hasExactAlarmsPermission

    fun clean() {
        hasPermission = false
        hasExactAlarmsPermission = false
    }
}
