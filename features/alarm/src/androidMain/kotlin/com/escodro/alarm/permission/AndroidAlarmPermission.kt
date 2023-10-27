package com.escodro.alarm.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.escodro.alarmapi.AlarmPermission

internal class AndroidAlarmPermission(
    private val context: Context,
    private val permissionChecker: PermissionChecker,
    private val sdkVersion: SdkVersion,
) : AlarmPermission {

    @SuppressLint("NewApi")
    override fun hasExactAlarmPermission(): Boolean =
        if (sdkVersion.currentVersion >= Build.VERSION_CODES.S) {
            permissionChecker.canScheduleExactAlarms()
        } else {
            true
        }

    @SuppressLint("NewApi")
    override fun hasNotificationPermission(): Boolean =
        if (sdkVersion.currentVersion >= Build.VERSION_CODES.TIRAMISU) {
            permissionChecker.checkPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }

    override fun openExactAlarmPermissionScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    }

    override fun openAppSettings() {
        val intent = Intent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}
