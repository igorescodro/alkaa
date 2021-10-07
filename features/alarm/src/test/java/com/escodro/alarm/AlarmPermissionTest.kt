package com.escodro.alarm

import android.app.AlarmManager
import android.os.Build
import com.escodro.alarm.fake.AndroidVersionFake
import com.escodro.alarm.permission.AlarmPermissionImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

internal class AlarmPermissionTest {

    private val alarmManager = mockk<AlarmManager>(relaxed = true)

    private val androidVersion = AndroidVersionFake()

    private val alarmPermission = AlarmPermissionImpl(alarmManager, androidVersion)

    @Test
    fun `test if when permission is granted returns true`() {
        every { alarmManager.canScheduleExactAlarms() } returns true
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasExactAlarmPermission()

        Assert.assertTrue(result)
    }

    @Test
    fun `test if when permission is not granted returns false`() {
        every { alarmManager.canScheduleExactAlarms() } returns false
        androidVersion.version = Build.VERSION_CODES.S

        val result = alarmPermission.hasExactAlarmPermission()

        Assert.assertFalse(result)
    }

    @Test
    fun `test if Android below S returns true`() {
        androidVersion.version = Build.VERSION_CODES.M

        val result = alarmPermission.hasExactAlarmPermission()

        Assert.assertTrue(result)
    }
}
