package com.escodro.alarm

import android.os.Build
import com.escodro.alarm.fake.AndroidVersionFake
import com.escodro.alarm.fake.PermissionCheckerFake
import com.escodro.alarm.fake.ScreenNavigatorFake
import com.escodro.alarm.permission.AndroidAlarmPermission
import org.junit.After
import org.junit.Assert
import org.junit.Test

internal class AlarmPermissionTest {

    private val screenNavigator = ScreenNavigatorFake()

    private val permissionChecker = PermissionCheckerFake()

    private val androidVersion = AndroidVersionFake()

    private val alarmPermission = AndroidAlarmPermission(
        screenNavigator = screenNavigator,
        permissionChecker = permissionChecker,
        sdkVersion = androidVersion,
    )

    @After
    fun tearDown() {
        permissionChecker.clean()
        androidVersion.clean()
    }

    @Test
    fun `test when exact alarm permission is granted then returns true`() {
        permissionChecker.hasExactAlarmsPermission = true
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasExactAlarmPermission()

        assertTrue(result)
    }

    @Test
    fun `test when exact alarm permission is not granted then returns false`() {
        permissionChecker.hasExactAlarmsPermission = false
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasExactAlarmPermission()

        assertFalse(result)
    }

    @Test
    fun `test when exact alarm on Android below S then returns true`() {
        permissionChecker.hasExactAlarmsPermission = false // Permission only available >= S
        androidVersion.version = Build.VERSION_CODES.M

        val result = alarmPermission.hasExactAlarmPermission()

        assertTrue(result)
    }
}
