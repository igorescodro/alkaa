package com.escodro.alarm.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.escodro.core.extension.getAlarmManager

internal class PermissionCheckerImpl(private val context: Context) : PermissionChecker {

    override fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.S)
    override fun canScheduleExactAlarms(): Boolean {
        val alarmManager = context.getAlarmManager() ?: return false
        return alarmManager.canScheduleExactAlarms()
    }
}
