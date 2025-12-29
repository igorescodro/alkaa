package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.usecase.alarm.implementation.UpdateAlarmImpl
import com.escodro.domain.usecase.factory.TaskFactory
import com.escodro.domain.usecase.fake.CancelAlarmFake
import com.escodro.domain.usecase.fake.ScheduleAlarmFake
import com.escodro.domain.usecase.fake.UpdateTaskAsRepeatingFake
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class UpdateAlarmTest {

    private val scheduleAlarm = ScheduleAlarmFake()

    private val updateTaskAsRepeating = UpdateTaskAsRepeatingFake()

    private val cancelAlarm = CancelAlarmFake()

    private val updateAlarm = UpdateAlarmImpl(
        scheduleAlarmUseCase = scheduleAlarm,
        updateTaskAsRepeatingUseCase = updateTaskAsRepeating,
        cancelAlarmUseCase = cancelAlarm,
    )

    @Test
    fun `test if alarm is set`() = runTest {
        // Given the alarm to be set
        val taskId = 123L
        val alarm = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val task = TaskFactory.createTask().copy(id = taskId, dueDate = alarm)

        // When the function to set the alarm is called
        updateAlarm(task)

        // Then the alarm is set
        assertTrue(scheduleAlarm.isAlarmScheduled(taskId))
        assertEquals(expected = alarm, actual = scheduleAlarm.getScheduledAlarm(taskId))
    }

    @Test
    fun `test if alarm is set as repeating`() = runTest {
        // Given the alarm to be set with interval
        val taskId = 123L
        val alarmInterval = AlarmInterval.WEEKLY
        val task = TaskFactory.createTask().copy(id = taskId, alarmInterval = alarmInterval)

        // When the function to set the alarm interval is called
        updateAlarm(task)

        // Then the alarm interval is set
        assertTrue(updateTaskAsRepeating.isAlarmUpdated(taskId))
        assertEquals(
            expected = alarmInterval,
            actual = updateTaskAsRepeating.getUpdatedAlarm(taskId)
        )
    }

    @Test
    fun `test if alarm is removed`() = runTest {
        // Given the alarm to be removed
        val taskId = 123L
        val task = TaskFactory.createTask().copy(id = taskId, dueDate = null)

        // When the function to cancel the alarm is called
        updateAlarm(task)

        // Then the alarm is removed
        assertTrue(actual = cancelAlarm.isAlarmCancelled(taskId))
    }

    @Test
    fun `test if alarm and interval are set`() = runTest {
        // Given the alarm and interval to be set
        val taskId = 123L
        val alarm = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val alarmInterval = AlarmInterval.WEEKLY
        val task = TaskFactory
            .createTask()
            .copy(id = taskId, dueDate = alarm, alarmInterval = alarmInterval)

        // When the function to set the alarm and interval is called
        updateAlarm(task)

        // Then the alarm and interval are set
        assertTrue(scheduleAlarm.isAlarmScheduled(taskId))
        assertEquals(expected = alarm, actual = scheduleAlarm.getScheduledAlarm(taskId))
        assertTrue(updateTaskAsRepeating.isAlarmUpdated(taskId))
        assertEquals(
            expected = alarmInterval,
            actual = updateTaskAsRepeating.getUpdatedAlarm(taskId)
        )
    }
}
