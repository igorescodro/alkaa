package com.escodro.domain.usecase.task

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CompleteTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockAlarmInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val mockCalendar = mockk<TaskCalendar>(relaxed = true)

    private val completeTask = CompleteTask(mockTaskRepo, mockAlarmInteractor, mockCalendar)

    @Test
    fun `check if task was completed`() = runBlockingTest {
        val currentTime = Calendar.getInstance()

        every { mockCalendar.getCurrentCalendar() } returns currentTime
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask

        completeTask(mockTask.id)

        val updatedTask = mockTask.copy(completed = true, completedDate = currentTime)
        coVerify { mockTaskRepo.updateTask(updatedTask) }
    }

    @Test
    fun `check if alarm was canceled`() = runBlockingTest {
        every { mockCalendar.getCurrentCalendar() } returns Calendar.getInstance()
        coEvery { mockTaskRepo.findTaskById(any()) } returns mockTask

        completeTask(mockTask.id)
        verify { mockAlarmInteractor.cancel(mockTask.id) }
    }
}
