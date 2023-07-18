package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import java.util.Calendar
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ScheduleNextAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val calendarProvider = CalendarProviderImpl()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, calendarProvider)

    private val baseTask = Task(1, title = "alarm task")

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if not repeating`() = runTest {
        val task = baseTask.copy(isRepeating = false)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if not repeating with valid due date`() = runTest {
        val task = baseTask.copy(isRepeating = false, dueDate = Calendar.getInstance())
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no due date`() = runTest {
        val task = baseTask.copy(dueDate = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no due date but it is repeating`() = runTest {
        val task = baseTask.copy(isRepeating = true, dueDate = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test if fails if no alarm interval`() = runTest {
        val task = baseTask.copy(dueDate = null, alarmInterval = null)
        scheduleNextAlarmUseCase(task)
    }

    @Test
    fun `test if alarm is updated to next hour`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.HOUR, 1)
        }

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next day`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next week`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.WEEKLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.DAY_OF_MONTH, 1)
        }

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next month`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.MONTHLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.MONTH, 1)
        }

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next year`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.YEARLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.apply {
            time = calendar.time
            add(Calendar.YEAR, 1)
        }

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if new alarm is scheduled`() = runTest {
        val task = baseTask.copy(
            isRepeating = true,
            dueDate = Calendar.getInstance(),
            alarmInterval = AlarmInterval.DAILY,
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)

        assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if missed repeating alarm is set on future`() = runTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }
        val task = baseTask.copy(
            dueDate = pastCalendar,
            completed = true,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY,
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = pastCalendar.apply {
            time = pastCalendar.time
            add(Calendar.HOUR, 5)
        }

        assertEquals(assertCalendar, result.dueDate)
    }
}
