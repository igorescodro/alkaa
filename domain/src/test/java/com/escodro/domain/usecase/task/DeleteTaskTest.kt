package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class DeleteTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val deleteTaskUseCase = DeleteTask(taskRepository, alarmInteractor)

    private val loadTaskUseCase = LoadTaskImpl(taskRepository)

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    @Before
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
    }

    @Test
    fun `test if task is deleted`() = runTest {
        val task = Task(id = 18, title = "coffee time")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        val loadedTask = loadTaskUseCase(task.id)

        Assert.assertNull(loadedTask)
    }

    @Test
    fun `test if the alarm is canceled when the task is completed`() = runTest {
        val task = Task(id = 19, title = "SOLID basics")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if the glance was notified`() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        Assert.assertTrue(glanceInteractor.wasNotified)
    }
}
