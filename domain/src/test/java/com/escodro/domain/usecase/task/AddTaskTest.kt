package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AddTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
    }

    @Test
    fun `test if task is correctly added`() = runBlockingTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        require(result != null)
        Assert.assertEquals(task, result)
    }

    @Test
    fun `test if task with empty title is not added`() = runBlockingTest {
        val task = Task(id = 15, title = " ", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        Assert.assertNull(result)
    }
}
