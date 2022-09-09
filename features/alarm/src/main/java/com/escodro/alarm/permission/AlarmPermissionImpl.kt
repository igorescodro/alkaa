package com.escodro.alarm.permission

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import com.escodro.alarmapi.AlarmPermission

internal class AlarmPermissionImpl(
    private val permissionChecker: PermissionChecker,
    private val androidVersion: AndroidVersion
) : AlarmPermission {

    @SuppressLint("NewApi")
    override fun hasExactAlarmPermission(): Boolean =
        if (androidVersion.currentVersion >= Build.VERSION_CODES.S) {
            permissionChecker.canScheduleExactAlarms()
        } else {
            true
        }

    override fun hasNotificationPermission(): Boolean =
        if (androidVersion.currentVersion >= Build.VERSION_CODES.TIRAMISU) {
            permissionChecker.checkPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }

    @Suppress("FunctionMaxLength")
    override fun shouldCheckNotificationPermission(): Boolean =
        androidVersion.currentVersion >= Build.VERSION_CODES.TIRAMISU
}
