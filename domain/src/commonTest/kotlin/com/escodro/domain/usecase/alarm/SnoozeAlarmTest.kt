package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.minutes

internal class SnoozeAlarmTest {

    private val calendarProvider = DateTimeProviderFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val snoozeAlarmUseCase =
        SnoozeAlarm(calendarProvider, notificationInteractor, alarmInteractor)

    private val baseTask = Task(id = 2345L, title = "it's time")

    @BeforeTest
    fun setup() = runTest {
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun test_if_task_is_snoozed() = runTest {
        val snoozeTime = 15

        snoozeAlarmUseCase(baseTask.id, snoozeTime)

        val calendarAssert = calendarProvider.getCurrentInstant().plus(snoozeTime.minutes)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val result = alarmInteractor.getAlarmTime(baseTask.id)
        val assert = calendarAssert.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        assertEquals(assert, result)
    }

    @Test
    fun test_if_error_is_shown_when_snoozing_with_negative_number() = runTest {
        assertFailsWith<IllegalArgumentException> {
            snoozeAlarmUseCase(baseTask.id, -15)
        }
    }

    @Test
    fun test_if_notification_is_dismissed() {
        notificationInteractor.show(baseTask)

        snoozeAlarmUseCase(baseTask.id, 15)

        notificationInteractor.isNotificationShown(baseTask.id)
    }
}
