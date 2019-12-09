package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class UncompleteTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val uncompleteTask = UncompleteTask(mockTaskRepo)

    @Test
    fun `check if task was uncompleted`() {
        uncompleteTask(mockTask).subscribe()

        val updatedTask = mockTask.copy(completed = false, completedDate = null)
        verify { mockTaskRepo.updateTask(updatedTask) }
    }
}
