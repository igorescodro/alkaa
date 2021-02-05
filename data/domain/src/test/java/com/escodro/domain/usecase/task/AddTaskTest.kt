package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AddTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
    }

    @Test
    fun `test if task is correctly added`() = runBlockingTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id).first()
        Assert.assertEquals(task, result)
    }

    @Test
    fun `test if task with empty title is not added`() = runBlockingTest {
        val task = Task(id = 15, title = " ", description = "this desc")
        addTaskUseCase(task)

        val resultList = mutableListOf<Task>()
        getTaskUseCase(task.id).collect { resultList.add(it) }

        Assert.assertEquals(0, resultList.size)
    }
}
