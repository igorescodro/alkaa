package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.UpdateAlarmFake
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
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class ScheduleNextAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val updateAlarm = UpdateAlarmFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val datetimeProvider = DateTimeProviderFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, updateAlarm, glanceInteractor)

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
    fun test_if_fails_if_not_repeating() = runTest {
        val task = baseTask.copy(isRepeating = false)

        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun test_if_fails_if_not_repeating_with_valid_due_date() = runTest {
        val task = baseTask.copy(
            isRepeating = false,
            dueDate = datetimeProvider.getCurrentLocalDateTime(),
        )
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun test_if_fails_if_no_due_date() = runTest {
        val task = baseTask.copy(dueDate = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun test_if_fails_if_no_due_date_but_it_is_repeating() = runTest {
        val task = baseTask.copy(isRepeating = true, dueDate = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun test_if_fails_if_no_alarm_interval() = runTest {
        val task = baseTask.copy(dueDate = null, alarmInterval = null)
        assertFailsWith<IllegalArgumentException> {
            scheduleNextAlarmUseCase(task)
        }
    }

    @Test
    fun test_if_alarm_is_updated_to_next_hour() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = calendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(1.hours)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }

    @Test
    fun test_if_alarm_is_updated_to_next_day() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = calendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(days = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }

    @Test
    fun test_if_alarm_is_updated_to_next_week() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.WEEKLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = calendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(days = 7), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }

    @Test
    fun test_if_alarm_is_updated_to_next_month() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.MONTHLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = calendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }

    @Test
    fun test_if_alarm_is_updated_to_next_year() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
        val task = baseTask.copy(
            dueDate = calendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.YEARLY,
        )
        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = calendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(DateTimePeriod(years = 1), TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }

    @Test
    fun test_if_new_alarm_is_scheduled() = runTest {
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
    fun test_if_missed_repeating_alarm_is_set_on_future() = runTest {
        val pastCalendar = datetimeProvider
            .getCurrentInstant()
            .minus(5.hours)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val task = baseTask.copy(
            dueDate = pastCalendar,
            isCompleted = true,
            isRepeating = true,
            alarmInterval = AlarmInterval.HOURLY,
        )

        addTaskUseCase(task)
        scheduleNextAlarmUseCase(task)
        val result = getTaskUseCase(task.id)

        requireNotNull(result)

        val assertCalendar = pastCalendar
            .toInstant(TimeZone.currentSystemDefault())
            .plus(5.hours)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        assertEquals(expected = assertCalendar, actual = result.dueDate)
    }
}
