package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UncompleteTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val uncompleteTask = UncompleteTask(mockTaskRepo)

    @Test
    fun `check if task was uncompleted`() = runBlockingTest {
        uncompleteTask(mockTask)

        val updatedTask = mockTask.copy(completed = false, completedDate = null)
        coEvery { mockTaskRepo.updateTask(updatedTask) }
    }
}
