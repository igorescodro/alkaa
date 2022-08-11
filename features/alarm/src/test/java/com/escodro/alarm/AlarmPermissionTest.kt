package com.escodro.alarm

import android.os.Build
import com.escodro.alarm.fake.AndroidVersionFake
import com.escodro.alarm.fake.PermissionCheckerFake
import com.escodro.alarm.permission.AlarmPermissionImpl
import org.junit.After
import org.junit.Assert
import org.junit.Test

internal class AlarmPermissionTest {

    private val permissionChecker = PermissionCheckerFake()

    private val androidVersion = AndroidVersionFake()

    private val alarmPermission = AlarmPermissionImpl(permissionChecker, androidVersion)

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

        Assert.assertTrue(result)
    }

    @Test
    fun `test when exact alarm permission is not granted then returns false`() {
        permissionChecker.hasExactAlarmsPermission = false
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasExactAlarmPermission()

        Assert.assertFalse(result)
    }

    @Test
    fun `test when exact alarm on Android below S then returns true`() {
        permissionChecker.hasExactAlarmsPermission = false // Permission only available >= S
        androidVersion.version = Build.VERSION_CODES.M

        val result = alarmPermission.hasExactAlarmPermission()

        Assert.assertTrue(result)
    }

    @Test
    fun `test when notification permission is granted then returns true`() {
        permissionChecker.hasPermission = true
        androidVersion.version = Build.VERSION_CODES.TIRAMISU

        val result = alarmPermission.hasNotificationPermission()

        Assert.assertTrue(result)
    }

    @Test
    fun `test when notification permission is not granted then returns false`() {
        permissionChecker.hasPermission = false
        androidVersion.version = Build.VERSION_CODES.TIRAMISU

        val result = alarmPermission.hasNotificationPermission()

        Assert.assertFalse(result)
    }

    @Test
    fun `test when notification permission on Android below T then returns true`() {
        permissionChecker.hasPermission = false // Permission only available >= S
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasNotificationPermission()

        Assert.assertTrue(result)
    }
}
