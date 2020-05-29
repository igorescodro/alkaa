package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.GetTask
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ScheduleNextAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val calendarProvider = CalendarProviderImpl()

    private val addTaskUseCase = AddTask(taskRepository)

    private val getTaskUseCase = GetTask(taskRepository)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, calendarProvider)

    private val baseTask = Task(1, title = "alarm task")

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if not repeating`() = runBlockingTest {
        val task = baseTask.copy(isRepeating = false)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if not repeating with valid due date`() = runBlockingTest {
        val task = baseTask.copy(isRepeating = false, dueDate = Calendar.getInstance())
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no due date`() = runBlocking {
        val task = baseTask.copy(dueDate = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no due date but it is repeating`() = runBlocking {
        val task = baseTask.copy(isRepeating = true, dueDate = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no alarm interval`() = runBlocking {
        val task = baseTask.copy(dueDate = null, alarmInterval = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test
    fun `test if alarm is updated to next hour`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.HOUR, 1)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next day`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next week`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.WEEKLY
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next month`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.MONTHLY
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.MONTH, 1)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next year`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.YEARLY
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.YEAR, 1)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if new alarm is scheduled`() = runBlockingTest {
        val task = baseTask.copy(
            isRepeating = true,
            dueDate = Calendar.getInstance(),
            alarmInterval = AlarmInterval.DAILY
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)

        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if missed repeating alarm is set on future`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }
        val task = baseTask.copy(
            dueDate = pastCalendar,
            completed = true,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id).first()

        val assertCalendar = pastCalendar.apply {
            time = pastCalendar.time
            add(Calendar.HOUR, 5)
        }

        Assert.assertEquals(assertCalendar, result.dueDate)
    }
}
