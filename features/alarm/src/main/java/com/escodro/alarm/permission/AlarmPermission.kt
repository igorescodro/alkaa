package com.escodro.alarm.permission

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Build

class AlarmPermission(
    private val alarmManager: AlarmManager?,
    private val androidVersion: AndroidVersion
) {

    /**
     * Verifies if the permission [android.Manifest.permission.SCHEDULE_EXACT_ALARM] is granted.
     *
     * @return `true` if the permission is granted, `false` otherwise
     */
    @SuppressLint("NewApi")
    fun hasExactAlarmPermission(): Boolean {
        if (alarmManager == null) return false

        return if (androidVersion.currentVersion >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}