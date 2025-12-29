package com.escodro.alarm.permission

import com.escodro.alarmapi.AlarmPermission

@Suppress("ForbiddenComment")
internal class DesktopAlarmPermission : AlarmPermission {

    @Suppress("ExpressionBodySyntax")
    override fun hasExactAlarmPermission(): Boolean {
        // TODO: Implement hasExactAlarmPermission
        return true
    }

    override fun openExactAlarmPermissionScreen() {
        // TODO: Implement openExactAlarmPermissionScreen
    }

    override fun openAppSettings() {
        // TODO: Implement openAppSettings
    }
}
