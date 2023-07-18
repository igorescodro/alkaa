package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProviderImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ShowAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val calendarProvider = CalendarProviderImpl()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, calendarProvider)

    private val showAlarmUseCase =
        ShowAlarm(taskRepository, notificationInteractor, scheduleNextAlarmUseCase)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun `test if alarm is shown when task is not yet completed`() = runTest {
        val task = Task(1, title = "should show", completed = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertTrue(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if alarm is ignored when task is already completed`() = runTest {
        val task = Task(2, title = "should not show", completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if next alarm is scheduled when task is repeating`() = runTest {
        val calendar = calendarProvider.getCurrentCalendar()
        val task = Task(
            3,
            title = "should repeat",
            isRepeating = true,
            dueDate = calendar,
            alarmInterval = AlarmInterval.YEARLY,
        )
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertTrue(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if next alarm is not scheduled when task is not repeating`() = runTest {
        val task = Task(4, title = "should not repeat", isRepeating = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if next alarm is not scheduled when task is completed`() = runTest {
        val task = Task(4, title = "it is already completed", isRepeating = true, completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }
}
