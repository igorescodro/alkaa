package com.escodro.alarm.permission

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.escodro.alarmapi.AlarmPermission

internal class AlarmPermissionImpl(
    private val context: Context,
    private val alarmManager: AlarmManager?,
    private val androidVersion: AndroidVersion
) : AlarmPermission {

    @SuppressLint("NewApi")
    override fun hasExactAlarmPermission(): Boolean {
        if (alarmManager == null) return false

        return if (androidVersion.currentVersion >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    override fun hasNotificationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
}
