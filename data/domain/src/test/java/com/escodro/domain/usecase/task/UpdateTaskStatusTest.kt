package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class UpdateTaskStatusTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockCompleteTask = mockk<CompleteTask>(relaxed = true)

    private val mockUncompleteTask = mockk<UncompleteTask>(relaxed = true)

    private val updateTaskStatus =
        UpdateTaskStatus(mockTaskRepo, mockCompleteTask, mockUncompleteTask)

    @Test
    fun `check if completed flag was inverted`() {
        every { mockTaskRepo.findTaskById(any()) } returns Single.just(mockTask)

        updateTaskStatus(mockTask.id).subscribe()
        verify { mockTask.completed.not() }
    }

    @Test
    fun `check if completed flow was called`() {
        every { mockTaskRepo.findTaskById(any()) } returns Single.just(mockTask)
        every { mockTask.completed } returns false

        updateTaskStatus(mockTask.id).subscribe()
        verify { mockCompleteTask(mockTask) }
    }

    @Test
    fun `check if uncompleted flow was called`() {
        every { mockTaskRepo.findTaskById(any()) } returns Single.just(mockTask)
        every { mockTask.completed } returns true

        updateTaskStatus(mockTask.id).subscribe()
        verify { mockUncompleteTask(mockTask) }
    }
}
