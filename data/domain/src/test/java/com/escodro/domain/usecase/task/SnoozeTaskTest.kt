package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.Calendar
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class SnoozeTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val snoozeTask = SnoozeTask(mockTaskRepo)

    @Test
    fun `check if task was snooze with positive number`() = runBlockingTest {
        coEvery { mockTaskRepo.findTaskById(mockTask.id) } returns mockTask

        val snoozeTime = 15
        snoozeTask(mockTask.id, snoozeTime)

        val snoozedCalendar = mockTask.dueDate?.apply { add(Calendar.MINUTE, snoozeTime) }
        val updatedTask = mockTask.copy(dueDate = snoozedCalendar)

        coEvery { mockTaskRepo.updateTask(updatedTask) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `check if task was not snooze with negative number`() = runBlockingTest {
        coEvery { mockTaskRepo.findTaskById(mockTask.id) } returns mockTask

        val snoozeTime = -15

        snoozeTask(mockTask.id, snoozeTime)
        coVerify(exactly = 0) { mockTaskRepo.updateTask(any()) }
    }
}
