package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val updateTask = UpdateTask(mockTaskRepo)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        updateTask(mockTask)
        coVerify { mockTaskRepo.updateTask(mockTask) }
    }
}
