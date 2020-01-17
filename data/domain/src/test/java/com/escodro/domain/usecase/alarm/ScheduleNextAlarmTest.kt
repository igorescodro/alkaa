package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ScheduleNextAlarmTest {

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockAlarmInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val scheduleNextAlarm = ScheduleNextAlarm(mockTaskRepo, mockAlarmInteractor)

    private val mockTask = Task(1, title = "alarm task")

    @Test(expected = IllegalArgumentException::class)
    fun `check if fails if not repeating`() = runBlockingTest {
        val task = mockTask.copy(isRepeating = false)
        scheduleNextAlarm(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if fails if not repeating with valid due date`() = runBlockingTest {
        val task = mockTask.copy(isRepeating = false, dueDate = Calendar.getInstance())
        scheduleNextAlarm(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if fails if no due date`() = runBlocking {
        val task = mockTask.copy(dueDate = null)
        scheduleNextAlarm(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if fails if no due date but it is repeating`() = runBlocking {
        val task = mockTask.copy(isRepeating = true, dueDate = null)
        scheduleNextAlarm(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if fails if no alarm interval`() = runBlocking {
        val task = mockTask.copy(dueDate = null, alarmInterval = null)
        scheduleNextAlarm(task)
    }

    @Test
    fun `check if alarm is updated to next hour`() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val calendarAssert = Calendar.getInstance().apply {
            time = calendar.time
            add(Calendar.HOUR, 1)
        }

        val task = mockTask.copy(
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.HOURLY
        )

        val taskAssert = task.copy(dueDate = calendarAssert)

        scheduleNextAlarm(task)
        coVerify { mockTaskRepo.updateTask(taskAssert) }
    }

    @Test
    fun `check if alarm is updated to next day`() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val calendarAssert = Calendar.getInstance().apply {
            time = calendar.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        val task = mockTask.copy(
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.DAILY
        )

        val taskAssert = task.copy(dueDate = calendarAssert)

        scheduleNextAlarm(task)
        coVerify { mockTaskRepo.updateTask(taskAssert) }
    }

    @Test
    fun `check if alarm is updated to next week`() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val calendarAssert = Calendar.getInstance().apply {
            time = calendar.time
            add(Calendar.WEEK_OF_MONTH, 1)
        }

        val task = mockTask.copy(
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.WEEKLY
        )

        val taskAssert = task.copy(dueDate = calendarAssert)

        scheduleNextAlarm(task)
        coVerify { mockTaskRepo.updateTask(taskAssert) }
    }

    @Test
    fun `check if alarm is updated to next month`() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val calendarAssert = Calendar.getInstance().apply {
            time = calendar.time
            add(Calendar.MONTH, 1)
        }

        val task = mockTask.copy(
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.MONTHLY
        )

        val taskAssert = task.copy(dueDate = calendarAssert)

        scheduleNextAlarm(task)
        coVerify { mockTaskRepo.updateTask(taskAssert) }
    }

    @Test
    fun `check if alarm is updated to next year`() = runBlockingTest {
        val calendar = Calendar.getInstance()
        val calendarAssert = Calendar.getInstance().apply {
            time = calendar.time
            add(Calendar.YEAR, 1)
        }

        val task = mockTask.copy(
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.YEARLY
        )

        val taskAssert = task.copy(dueDate = calendarAssert)

        scheduleNextAlarm(task)
        coVerify { mockTaskRepo.updateTask(taskAssert) }
    }

    @Test
    fun `check if new alarm is scheduled`() = runBlockingTest {
        val task = mockTask.copy(
            isRepeating = true,
            dueDate = Calendar.getInstance(),
            alarmInterval = AlarmInterval.DAILY
        )

        scheduleNextAlarm(task)
        verify { mockAlarmInteractor.schedule(task.id, any()) }
    }

    @Test
    fun `check if missed repeating alarm is set on future`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }
        val task = Task(
            id = 1,
            title = "lost",
            dueDate = pastCalendar,
            completed = true,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY
        )

        val calendarAssert = Calendar.getInstance().apply {
            time = pastCalendar.time
            add(Calendar.HOUR, 5)
        }

        scheduleNextAlarm(task)
        verify { mockAlarmInteractor.schedule(task.id, calendarAssert.time.time) }
    }
}
