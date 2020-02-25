package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class RescheduleFutureAlarmsTest {

    private val mockRepo = mockk<TaskRepository>(relaxed = true)

    private val mockAlarmInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val mockCalendar = mockk<CalendarProvider>(relaxed = true)

    private val mockNextAlarm = mockk<ScheduleNextAlarm>(relaxed = true)

    private val rescheduleFutureAlarms =
        RescheduleFutureAlarms(mockRepo, mockAlarmInteractor, mockCalendar, mockNextAlarm)

    @Before
    fun setup() {
        every { mockCalendar.getCurrentCalendar() } returns Calendar.getInstance()
    }

    @Test
    fun `check if only tasks in the future are shown`() = runBlockingTest {
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, title = "Title", dueDate = futureCalendar)
        val task2 = Task(id = 2, title = "Title")
        val task3 = Task(id = 3, title = "Title", dueDate = futureCalendar)
        val task4 = Task(id = 4, title = "Title")
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)

        coEvery { mockRepo.findAllTasksWithDueDate() } returns repoList

        rescheduleFutureAlarms()
        verify { mockAlarmInteractor.schedule(task1.id, task1.dueDate!!.time.time) }
        verify { mockAlarmInteractor.schedule(task3.id, task3.dueDate!!.time.time) }
        verify { mockAlarmInteractor.schedule(task5.id, task5.dueDate!!.time.time) }
    }

    @Test
    fun `check if completed tasks are ignored`() = runBlockingTest {
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, completed = true, title = "Title", dueDate = futureCalendar)
        val task2 = Task(id = 2, completed = true, title = "Title", dueDate = futureCalendar)
        val task3 = Task(id = 3, completed = true, title = "Title", dueDate = futureCalendar)
        val task4 = Task(id = 4, completed = false, title = "Title", dueDate = futureCalendar)
        val task5 = Task(id = 5, completed = false, title = "Title", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)

        coEvery { mockRepo.findAllTasksWithDueDate() } returns repoList

        rescheduleFutureAlarms()
        verify { mockAlarmInteractor.schedule(task4.id, task4.dueDate!!.time.time) }
        verify { mockAlarmInteractor.schedule(task5.id, task5.dueDate!!.time.time) }
    }

    @Test
    fun `check if uncompleted tasks on the past are ignored`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -15) }
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, title = "Title", dueDate = pastCalendar)
        val task2 = Task(id = 2, title = "Title", dueDate = pastCalendar)
        val task3 = Task(id = 3, title = "Title", dueDate = pastCalendar)
        val task4 = Task(id = 4, title = "Title", dueDate = pastCalendar)
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)
        val repoList = listOf(task1, task2, task3, task4, task5)

        coEvery { mockRepo.findAllTasksWithDueDate() } returns repoList

        rescheduleFutureAlarms()
        verify { mockAlarmInteractor.schedule(task5.id, task5.dueDate!!.time.time) }
    }

    @Test
    fun `check if task is rescheduled`() = runBlockingTest {
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }
        val task = Task(id = 1, title = "Reschedule", dueDate = futureCalendar)

        coEvery { mockRepo.findAllTasksWithDueDate() } returns listOf(task)
        rescheduleFutureAlarms()
        verify { mockAlarmInteractor.schedule(task.id, futureCalendar.time.time) }
    }

    @Test
    fun `check if missed repeating alarms are rescheduled`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val task = Task(
            id = 1,
            title = "lost",
            dueDate = pastCalendar,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY
        )

        coEvery { mockRepo.findAllTasksWithDueDate() } returns listOf(task)
        rescheduleFutureAlarms()
        coVerify { mockNextAlarm(task) }
    }

    @Test
    fun `check if completed missed repeating alarms are ignored`() = runBlockingTest {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val task = Task(
            id = 1,
            title = "lost",
            dueDate = pastCalendar,
            completed = true,
            isRepeating = true,
            alarmInterval = AlarmInterval.DAILY
        )

        coEvery { mockRepo.findAllTasksWithDueDate() } returns listOf(task)
        rescheduleFutureAlarms()
        coVerify(exactly = 0) { mockNextAlarm(task) }
    }
}
