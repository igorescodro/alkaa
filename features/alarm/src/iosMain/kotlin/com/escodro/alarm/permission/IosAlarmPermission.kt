package com.escodro.alarm.permission

import com.escodro.alarmapi.AlarmPermission
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

internal class IosAlarmPermission : AlarmPermission {

    override fun hasExactAlarmPermission(): Boolean =
        true // iOS does not have exact alarm permission

    override fun openExactAlarmPermissionScreen() {
        throw UnsupportedOperationException("iOS does not have exact alarm permission")
    }

    override fun openAppSettings() {
        UIApplication.sharedApplication.openURL(NSURL(string = UIApplicationOpenSettingsURLString))
    }
}
