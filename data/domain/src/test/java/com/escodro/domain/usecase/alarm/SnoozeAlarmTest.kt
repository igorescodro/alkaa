package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.CalendarProviderFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SnoozeAlarmTest {

    private val calendarProvider = CalendarProviderFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val snoozeAlarmUseCase =
        SnoozeAlarm(calendarProvider, notificationInteractor, alarmInteractor)

    private val baseTask = Task(id = 2345L, title = "it's time")

    @Before
    fun setup() = runBlockingTest {
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun `test if task is snoozed`() = runBlockingTest {
        val snoozeTime = 15

        snoozeAlarmUseCase(baseTask.id, snoozeTime)

        val calendarAssert = Calendar.getInstance().apply {
            time = calendarProvider.getCurrentCalendar().time
            add(Calendar.MINUTE, snoozeTime)
        }
        val result = alarmInteractor.getAlarmTime(baseTask.id)
        Assert.assertEquals(calendarAssert.time.time, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if error is shown when snoozing with negative number`() = runBlockingTest {
        snoozeAlarmUseCase(baseTask.id, -15)
    }

    @Test
    fun `test if notification is dismissed`() {
        notificationInteractor.show(baseTask)

        snoozeAlarmUseCase(baseTask.id, 15)

        notificationInteractor.isNotificationShown(baseTask.id)
    }
}
