package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
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

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val addTaskUseCase = AddTask(taskRepository)

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

        val resultList = mutableListOf<Task>()
        getTaskUseCase(task.id).collect { resultList.add(it) }

        Assert.assertEquals(0, resultList.size)
    }

    @Test
    fun `test if the alarm is canceled when the task is completed`() = runBlockingTest {
        val task = Task(id = 19, title = "SOLID basics")
        addTaskUseCase(task)
        deleteTaskUseCase(task)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }
}
