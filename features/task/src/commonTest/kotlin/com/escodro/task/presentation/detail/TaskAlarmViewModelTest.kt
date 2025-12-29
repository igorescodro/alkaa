package com.escodro.task.presentation.detail

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.task.presentation.detail.main.TaskId
import com.escodro.task.presentation.factory.TaskFactory
import com.escodro.task.presentation.fake.LoadTaskFake
import com.escodro.task.presentation.fake.UpdateAlarmFake
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class TaskAlarmViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val updateAlarm = UpdateAlarmFake()

    private val loadTask = LoadTaskFake()

    private val alarmIntervalMapper = AlarmIntervalMapper()

    private val viewModel = TaskAlarmViewModel(
        updateAlarm = updateAlarm,
        loadTask = loadTask,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
        alarmIntervalMapper = alarmIntervalMapper,
    )

    @Test
    fun `test if alarm is set`() = runTest {
        // Given the alarm to be set
        val taskId = 123L
        val alarm = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val task = TaskFactory.createTask(id = taskId, dueDate = null)
        loadTask.taskToBeReturned = task

        // When the function to set the alarm is called
        viewModel.updateAlarm(TaskId(taskId), alarm)

        // Then the alarm is set
        assertEquals(expected = alarm, actual = updateAlarm.getScheduledTask(taskId)!!.dueDate)
    }

    @Test
    fun `test if alarm is set as repeating`() = runTest {
        // Given the alarm to be set with interval
        val taskId = 123L
        val alarmInterval = AlarmInterval.WEEKLY
        val task = TaskFactory.createTask(id = taskId, dueDate = null)
        loadTask.taskToBeReturned = task

        // When the function to set the alarm interval is called
        viewModel.setRepeating(TaskId(taskId), alarmInterval)

        // Then the alarm interval is set
        val assertInterval = alarmIntervalMapper.toDomain(alarmInterval)
        assertEquals(
            expected = assertInterval,
            actual = updateAlarm.getScheduledTask(taskId)!!.alarmInterval,
        )
    }

    @Test
    fun `test if alarm is removed`() = runTest {
        // Given the alarm to be removed
        val taskId = 123L
        val dueDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val task = TaskFactory.createTask(id = taskId, dueDate = dueDate)
        loadTask.taskToBeReturned = task

        // When the function to cancel the alarm is called
        viewModel.updateAlarm(TaskId(taskId), null)

        // Then the alarm is removed
        assertNull(updateAlarm.getScheduledTask(taskId)!!.dueDate)
    }
}
