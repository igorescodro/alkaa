package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.UpdateAlarmFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class DeleteTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val updateAlarm = UpdateAlarmFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val deleteTaskUseCase = DeleteTask(taskRepository, alarmInteractor)

    private val loadTaskUseCase = LoadTaskImpl(taskRepository)

    private val addTaskUseCase = AddTaskImpl(taskRepository, updateAlarm, glanceInteractor)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
    }

    @Test
    fun test_if_task_is_deleted() = runTest {
        val task = Task(id = 18, title = "coffee time")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        val loadedTask = loadTaskUseCase(task.id)

        assertNull(loadedTask)
    }

    @Test
    fun test_if_the_alarm_is_canceled_when_the_task_is_completed() = runTest {
        val task = Task(id = 19, title = "SOLID basics")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun test_if_the_glance_was_notified() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        assertTrue(glanceInteractor.isNotified)
    }
}
