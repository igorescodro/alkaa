package com.escodro.domain.usecase.task

import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class UncompleteTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<ViewData.Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockGetTask = mockk<GetTask>(relaxed = true)

    private val mockUpdateTask = mockk<UpdateTask>(relaxed = true)

    private val uncompleteTask = UncompleteTask(mockTaskRepo, mockGetTask, mockUpdateTask)

    @Test
    fun `check if task was uncompleted`() {
        every { mockGetTask.invoke(any()) } returns Single.just(mockTask)

        uncompleteTask(mockTask.id).subscribe()

        verify { mockTask.completed = false }
        verify { mockTask.completedDate = null }
        verify { mockUpdateTask.invoke(any()) }
    }
}
