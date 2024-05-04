package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.UpdateAlarmFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ShowAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val updateAlarm = UpdateAlarmFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val datetimeProvider = DateTimeProviderFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, updateAlarm, glanceInteractor)

    private val scheduleNextAlarmUseCase =
        ScheduleNextAlarm(taskRepository, alarmInteractor, datetimeProvider)

    private val showAlarmUseCase =
        ShowAlarm(taskRepository, notificationInteractor, scheduleNextAlarmUseCase)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun test_if_alarm_is_shown_when_task_is_not_yet_completed() = runTest {
        val task = Task(1, title = "should show", completed = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertTrue(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun test_if_alarm_is_ignored_when_task_is_already_completed() = runTest {
        val task = Task(2, title = "should not show", completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun test_if_next_alarm_is_scheduled_when_task_is_repeating() = runTest {
        val calendar = datetimeProvider.getCurrentLocalDateTime()
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
    fun test_if_next_alarm_is_not_scheduled_when_task_is_not_repeating() = runTest {
        val task = Task(4, title = "should not repeat", isRepeating = false)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun test_if_next_alarm_is_not_scheduled_when_task_is_completed() = runTest {
        val task = Task(4, title = "it is already completed", isRepeating = true, completed = true)
        addTaskUseCase(task)

        showAlarmUseCase(task.id)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }
}
