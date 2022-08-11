package com.escodro.alarm.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.escodro.core.extension.getAlarmManager

internal class PermissionCheckerImpl(private val context: Context) : PermissionChecker {

    override fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    override fun canScheduleExactAlarms(): Boolean {
        val alarmManager = context.getAlarmManager() ?: return false
        return alarmManager.canScheduleExactAlarms()
    }
}
