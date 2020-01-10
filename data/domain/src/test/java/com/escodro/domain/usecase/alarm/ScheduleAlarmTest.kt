package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import org.junit.Test

class ScheduleAlarmTest {

    private val mockInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val scheduleAlarm = ScheduleAlarm(mockInteractor)

    @Test
    fun `check if interactor function was called`() {
        val alarmId = 123L
        val time = Calendar.getInstance().timeInMillis

        scheduleAlarm(alarmId, time)
        verify { mockInteractor.schedule(alarmId, time) }
    }
}
