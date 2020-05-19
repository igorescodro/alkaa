package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.AddTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ShowAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val calendarProvider = CalendarProviderImpl()

    private val addTaskUseCase = AddTask(taskRepository)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, calendarProvider)

    private val showAlarmUseCase =
        ShowAlarm(taskRepository, notificationInteractor, scheduleNextAlarmUseCase)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun `test if alarm is shown when task is not yet completed`() = runBlockingTest {
        val task = Task(1, title = "should show", completed = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        Assert.assertTrue(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if alarm is ignored when task is already completed`() = runBlockingTest {
        val task = Task(2, title = "should not show", completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        Assert.assertFalse(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if next alarm is scheduled when task is repeating`() = runBlockingTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = Task(
            3,
            title = "should repeat",
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.YEARLY
        )
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        Assert.assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if next alarm is not scheduled when task is not repeating`() = runBlockingTest {
        val task = Task(4, title = "should not repeat", isRepeating = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if next alarm is not scheduled when task is completed`() = runBlockingTest {
        val task = Task(4, title = "it is already completed", isRepeating = true, completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }
}
