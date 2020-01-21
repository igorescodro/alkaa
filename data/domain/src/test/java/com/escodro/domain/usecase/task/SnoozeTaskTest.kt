package com.escodro.domain.usecase.task

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import org.junit.Before
import org.junit.Test

class SnoozeTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val calendarProvider = mockk<CalendarProvider>(relaxed = true)

    private val mockNotificationInteractor = mockk<NotificationInteractor>(relaxed = true)

    private val mockAlarmInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val snoozeTask =
        SnoozeTask(calendarProvider, mockNotificationInteractor, mockAlarmInteractor)

    @Before
    fun setup() {
        every { calendarProvider.getCurrentCalendar() } returns Calendar.getInstance()
    }

    @Test
    fun `check if task was snooze with positive number`() {
        val snoozeTime = 15

        val calendarAssert = Calendar.getInstance().apply {
            time = calendarProvider.getCurrentCalendar().time
            add(Calendar.MINUTE, snoozeTime)
        }

        snoozeTask(mockTask.id, snoozeTime)

        verify { mockAlarmInteractor.schedule(mockTask.id, calendarAssert.time.time) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if task was not snooze with negative number`() {
        val snoozeTime = -15

        snoozeTask(mockTask.id, snoozeTime)
        verify(exactly = 0) { mockAlarmInteractor.schedule(any(), any()) }
    }

    @Test
    fun `check if notification is dismissed`() {
        snoozeTask(mockTask.id)
        verify { mockNotificationInteractor.dismiss(mockTask.id) }
    }
}
