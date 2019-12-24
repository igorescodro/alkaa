package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class AddTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val addTask = AddTask(mockTaskRepo)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        addTask(mockTask)
        coVerify { mockTaskRepo.insertTask(mockTask) }
    }
}
