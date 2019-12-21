package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateTaskStatusTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockCompleteTask = mockk<CompleteTask>(relaxed = true)

    private val mockUncompleteTask = mockk<UncompleteTask>(relaxed = true)

    private val updateTaskStatus =
        UpdateTaskStatus(mockTaskRepo, mockCompleteTask, mockUncompleteTask)

    @Test
    fun `check if completed flag was inverted`() = runBlockingTest {
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask

        updateTaskStatus(mockTask.id)
        verify { mockTask.completed.not() }
    }

    @Test
    fun `check if completed flow was called`() = runBlockingTest {
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask
        coEvery { mockTask.completed } returns false

        updateTaskStatus(mockTask.id)
        coVerify { mockCompleteTask(mockTask) }
    }

    @Test
    fun `check if uncompleted flow was called`() = runBlockingTest {
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask
        coEvery { mockTask.completed } returns true

        updateTaskStatus(mockTask.id)
        coVerify { mockUncompleteTask(mockTask) }
    }
}
