package com.escodro.domain.usecase.task

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.util.Calendar
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CompleteTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockCalendar = mockk<TaskCalendar>(relaxed = true)

    private val completeTask = CompleteTask(mockTaskRepo, mockCalendar)

    @Test
    fun `check if task was completed`() = runBlockingTest {
        val currentTime = Calendar.getInstance()

        every { mockCalendar.getCurrentCalendar() } returns currentTime
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask

        completeTask(mockTask.id)

        val updatedTask = mockTask.copy(completed = true, completedDate = currentTime)
        coVerify { mockTaskRepo.updateTask(updatedTask) }
    }
}
