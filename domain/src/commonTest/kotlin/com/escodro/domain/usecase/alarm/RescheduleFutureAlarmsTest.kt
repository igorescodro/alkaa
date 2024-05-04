package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.UpdateAlarmFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

internal class RescheduleFutureAlarmsTest {

    private val taskRepository = TaskRepositoryFake()

    private val updateAlarm = UpdateAlarmFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val dateTimeProvider = DateTimeProviderFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, updateAlarm, glanceInteractor)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, dateTimeProvider)

    private val rescheduleAlarmsUseCase =
        RescheduleFutureAlarms(
            taskRepository,
            alarmInteractor,
            dateTimeProvider,
            scheduleNextAlarmUseCase,
        )

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun test_if_future_alarms_are_rescheduled() = runTest {
        val futureCalendar = dateTimeProvider.getCurrentInstant()
            .plus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val task1 = Task(id = 1, title = "Task 1", dueDate = futureCalendar)
        val task2 = Task(id = 2, title = "Task 2")
        val task3 = Task(id = 3, title = "Task 3", dueDate = futureCalendar)
        val task4 = Task(id = 4, title = "Task 4")
        val task5 = Task(id = 5, title = "Task 5", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        assertTrue(alarmInteractor.isAlarmScheduled(task1.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        assertTrue(alarmInteractor.isAlarmScheduled(task3.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task4.id))
        assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun test_if_completed_tasks_are_not_rescheduled() = runTest {
        val futureCalendar = dateTimeProvider.getCurrentInstant()
            .plus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val task1 = Task(id = 1, completed = true, title = "Task 1", dueDate = futureCalendar)
        val task2 = Task(id = 2, completed = true, title = "Task 2", dueDate = futureCalendar)
        val task3 = Task(id = 3, completed = true, title = "Task 3", dueDate = futureCalendar)
        val task4 = Task(id = 4, completed = false, title = "Task 4", dueDate = futureCalendar)
        val task5 = Task(id = 5, completed = false, title = "Task 5", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        assertFalse(alarmInteractor.isAlarmScheduled(task1.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task3.id))
        assertTrue(alarmInteractor.isAlarmScheduled(task4.id))
        assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun test_if_uncompleted_tasks_on_the_past_are_ignored() = runTest {
        val pastCalendar = dateTimeProvider.getCurrentInstant()
            .minus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val futureCalendar = dateTimeProvider.getCurrentInstant()
            .plus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val task1 = Task(id = 1, title = "Title", dueDate = pastCalendar)
        val task2 = Task(id = 2, title = "Title", dueDate = pastCalendar)
        val task3 = Task(id = 3, title = "Title", dueDate = pastCalendar)
        val task4 = Task(id = 4, title = "Title", dueDate = pastCalendar)
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        assertFalse(alarmInteractor.isAlarmScheduled(task1.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task3.id))
        assertFalse(alarmInteractor.isAlarmScheduled(task4.id))
        assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun test_if_missed_repeating_alarms_are_rescheduled() = runTest {
        val pastCalendar = dateTimeProvider.getCurrentInstant()
            .minus(1.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val task = Task(
            id = 1,
            title = "lost",
            dueDate = pastCalendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY,
        )

        addTaskUseCase(task)
        rescheduleAlarmsUseCase()
        assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }
}
