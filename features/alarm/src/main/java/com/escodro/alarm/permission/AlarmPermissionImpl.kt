package com.escodro.alarm.permission

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Build
import com.escodro.alarmapi.AlarmPermission

internal class AlarmPermissionImpl(
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
}
