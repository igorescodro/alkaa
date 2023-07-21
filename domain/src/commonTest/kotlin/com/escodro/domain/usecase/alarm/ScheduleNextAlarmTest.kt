package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

internal class ScheduleNextAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val datetimeProvider = DateTimeProviderFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, datetimeProvider)

    private val baseTask = Task(1, title = "alarm task")

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun `test if fails if not repeating`() = runTest {
        val task = baseTask.copy(isRepeating = false)

        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun `test if fails if not repeating with valid due date`() = runTest {
        val task = baseTask.copy(
            isRepeating = false,
            dueDate = datetimeProvider.getCurrentLocalDateTime(),
        )
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun `test if fails if no due date`() = runTest {
        val task = baseTask.copy(dueDate = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun `test if fails if no due date but it is repeating`() = runTest {
        val task = baseTask.copy(isRepeating = true, dueDate = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun `test if fails if no alarm interval`() = runTest {
        val task = baseTask.copy(dueDate = null, alarmInterval = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun `test if alarm is updated to next hour`() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.toInstant(TimeZone.currentSystemDefault())
            .plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next day`() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(days = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next week`() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.WEEKLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(days = 7), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next month`() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.MONTHLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if alarm is updated to next year`() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.YEARLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        require(result != null)

        val assertCalendar = calendar.toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(years = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }

    @Test
    fun `test if new alarm is scheduled`() = runTest {
        val task = baseTask.copy(
            isRepeating = true,
            dueDate = datetimeProvider.getCurrentLocalDateTime(),
            alarmInterval = AlarmInterval.DAILY,
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)

        assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if missed repeating alarm is set on future`() = runTest {
        val pastCalendar = datetimeProvider.getCurrentInstant()
            .minus(5.hours).toLocalDateTime(TimeZone.currentSystemDefault())

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

        val assertCalendar = pastCalendar.toInstant(TimeZone.currentSystemDefault())
            .plus(5.hours).toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(assertCalendar, result.dueDate)
    }
}
