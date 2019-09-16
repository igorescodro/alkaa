package com.escodro.domain.usecase.task

import com.escodro.domain.viewdata.ViewData
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class UpdateTaskStatusTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<ViewData.Task>(relaxed = true)

    private val mockCompleteTask = mockk<CompleteTask>(relaxed = true)

    private val mockUncompleteTask = mockk<UncompleteTask>(relaxed = true)

    private val updateTaskStatus = UpdateTaskStatus(mockCompleteTask, mockUncompleteTask)

    @Test
    fun `check if completed flag was inverted`() {
        updateTaskStatus(mockTask)
        verify { mockTask.completed = !mockTask.completed }
    }

    @Test
    fun `check if completed flow was called`() {
        every { mockTask.completed } returns false andThen true

        updateTaskStatus(mockTask)
        verify { mockTask.completed = true }
        verify { mockCompleteTask(mockTask.id) }
    }

    @Test
    fun `check if uncompleted flow was called`() {
        every { mockTask.completed } returns true andThen false

        updateTaskStatus(mockTask)
        verify { mockTask.completed = false }
        verify { mockUncompleteTask(mockTask.id) }
    }
}
