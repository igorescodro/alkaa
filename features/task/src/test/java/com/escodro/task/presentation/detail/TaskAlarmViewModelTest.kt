package com.escodro.task.presentation.detail

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.main.TaskId
import com.escodro.task.presentation.fake.CancelAlarmFake
import com.escodro.task.presentation.fake.ScheduleAlarmFake
import com.escodro.task.presentation.fake.UpdateTaskAsRepeatingFake
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

internal class TaskAlarmViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val scheduleAlarm = ScheduleAlarmFake()

    private val updateTaskAsRepeating = UpdateTaskAsRepeatingFake()

    private val cancelAlarm = CancelAlarmFake()

    private val alarmIntervalMapper = AlarmIntervalMapper()

    private val viewModel =
        TaskAlarmViewModel(scheduleAlarm, updateTaskAsRepeating, cancelAlarm, alarmIntervalMapper)

    @Test
    fun `test if alarm is set`() = runBlockingTest {
        // Given the alarm to be set
        val taskId = 123L
        val alarm = Calendar.getInstance()

        // When the function to set the alarm is called
        viewModel.updateAlarm(TaskId(taskId), alarm)

        // Then the alarm is set
        Assert.assertTrue(scheduleAlarm.isAlarmScheduled(taskId))
        Assert.assertEquals(alarm, scheduleAlarm.getScheduledAlarm(taskId))
    }

    @Test
    fun `test if alarm is set as repeating`() = runBlockingTest {
        // Given the alarm to be set with interval
        val taskId = 123L
        val alarmInterval = AlarmInterval.WEEKLY

        // When the function to set the alarm interval is called
        viewModel.setRepeating(TaskId(taskId), alarmInterval)

        // Then the alarm interval is set
        Assert.assertTrue(updateTaskAsRepeating.isAlarmUpdated(taskId))
        val assertInterval = alarmIntervalMapper.toDomain(alarmInterval)
        Assert.assertEquals(assertInterval, updateTaskAsRepeating.getUpdatedAlarm(taskId))
    }

    @Test
    fun `test if alarm is removed`() = runBlockingTest {
        // Given the alarm to be removed
        val taskId = 123L

        // When the function to cancel the alarm is called
        viewModel.updateAlarm(TaskId(taskId), null)

        // Then the alarm is removed
        Assert.assertTrue(cancelAlarm.isAlarmCancelled(taskId))
    }
}
