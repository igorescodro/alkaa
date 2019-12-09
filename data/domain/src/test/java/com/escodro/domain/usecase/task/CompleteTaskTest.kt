package com.escodro.domain.usecase.task

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import java.util.Calendar
import org.junit.Rule
import org.junit.Test

class CompleteTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockCalendar = mockk<TaskCalendar>(relaxed = true)

    private val completeTask = CompleteTask(mockTaskRepo, mockCalendar)

    @Test
    fun `check if task was completed`() {
        val currentTime = Calendar.getInstance()

        every { mockCalendar.getCurrentCalendar() } returns currentTime
        every { mockTaskRepo.findTaskById(any()) } returns Single.just(mockTask)

        completeTask(mockTask.id).subscribe()

        val updatedTask = mockTask.copy(completed = true, completedDate = currentTime)
        verify { mockTaskRepo.updateTask(updatedTask) }
    }
}
