package com.escodro.alarm.permission

/**
 * Interface to navigate between settings screens.
 */
internal interface ScreenNavigator {

    /**
     * Opens the screen to request the exact alarm permission.
     */
    fun openExactAlarmPermissionScreen()

    /**
     * Opens the app settings screen.
     */
    fun openAppSettings()
}
