package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UpdateTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val updateTaskUseCase = UpdateTask(taskRepository)

    private val getTaskUseCase = GetTask(taskRepository)

    @Test
    fun `test if task is updated`() = runBlockingTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val updatedTask = task.copy(title = "it's funny", description = "my mistake!")
        updateTaskUseCase(updatedTask)

        val result = getTaskUseCase(task.id).first()
        Assert.assertEquals(updatedTask, result)
    }
}
