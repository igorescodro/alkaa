package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DeleteTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val deleteTaskUseCase = DeleteTask(taskRepository, alarmInteractor)

    private val loadTaskUseCase = LoadTaskImpl(taskRepository)

    private val addTaskUseCase = AddTaskImpl(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun `test if task is deleted`() = runBlockingTest {
        val task = Task(id = 18, title = "coffee time")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        val loadedTask = loadTaskUseCase(task.id)

        Assert.assertNull(loadedTask)
    }

    @Test
    fun `test if the alarm is canceled when the task is completed`() = runBlockingTest {
        val task = Task(id = 19, title = "SOLID basics")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }
}
