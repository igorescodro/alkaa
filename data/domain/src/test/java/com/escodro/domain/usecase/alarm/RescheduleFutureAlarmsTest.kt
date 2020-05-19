package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.CalendarProviderFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.AddTask
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class RescheduleFutureAlarmsTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val calendarProvider = CalendarProviderFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, calendarProvider)

    private val rescheduleAlarmsUseCase =
        RescheduleFutureAlarms(
            taskRepository,
            alarmInteractor,
            calendarProvider,
            scheduleNextAlarmUseCase
        )

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun `test if future alarms are rescheduled`() = runBlockingTest {
        val futureCalendar =
            calendarProvider.getCurrentCalendar().apply { add(Calendar.DAY_OF_MONTH, 15) }
        val task1 = Task(id = 1, title = "Task 1", dueDate = futureCalendar)
        val task2 = Task(id = 2, title = "Task 2")
        val task3 = Task(id = 3, title = "Task 3", dueDate = futureCalendar)
        val task4 = Task(id = 4, title = "Task 4")
        val task5 = Task(id = 5, title = "Task 5", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task1.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task3.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task4.id))
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun `test if completed tasks are not rescheduled`() = runBlockingTest {
        val futureCalendar =
            calendarProvider.getCurrentCalendar().apply { add(Calendar.DAY_OF_MONTH, 15) }
        val task1 = Task(id = 1, completed = true, title = "Task 1", dueDate = futureCalendar)
        val task2 = Task(id = 2, completed = true, title = "Task 2", dueDate = futureCalendar)
        val task3 = Task(id = 3, completed = true, title = "Task 3", dueDate = futureCalendar)
        val task4 = Task(id = 4, completed = false, title = "Task 4", dueDate = futureCalendar)
        val task5 = Task(id = 5, completed = false, title = "Task 5", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task1.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task3.id))
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task4.id))
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun `test if uncompleted tasks on the past are ignored`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply {
            time = calendarProvider.getCurrentCalendar().time
            add(Calendar.DAY_OF_MONTH, -15)
        }
        val futureCalendar = Calendar.getInstance().apply {
            time = calendarProvider.getCurrentCalendar().time
            add(Calendar.DAY_OF_MONTH, 15)
        }

        val task1 = Task(id = 1, title = "Title", dueDate = pastCalendar)
        val task2 = Task(id = 2, title = "Title", dueDate = pastCalendar)
        val task3 = Task(id = 3, title = "Title", dueDate = pastCalendar)
        val task4 = Task(id = 4, title = "Title", dueDate = pastCalendar)
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)
        repoList.forEach { task -> addTaskUseCase(task) }

        rescheduleAlarmsUseCase()

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task1.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task2.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task3.id))
        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task4.id))
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task5.id))
    }

    @Test
    fun `test if missed repeating alarms are rescheduled`() = runBlockingTest {
        val pastCalendar =
            calendarProvider.getCurrentCalendar().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val task = Task(
            id = 1,
            title = "lost",
            dueDate = pastCalendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY
        )

        addTaskUseCase(task)
        rescheduleAlarmsUseCase()
        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }
}
