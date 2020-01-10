package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CancelAlarmTest {

    private val mockInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val cancelAlarm = CancelAlarm(mockInteractor)

    @Test
    fun `check if interactor function was called`() {
        val alarmId = 123L

        cancelAlarm(alarmId)
        verify { mockInteractor.cancel(alarmId) }
    }
}
