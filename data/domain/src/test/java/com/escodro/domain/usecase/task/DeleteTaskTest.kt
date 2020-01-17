package com.escodro.domain.usecase.task

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class DeleteTaskTest {

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockAlarmInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val deleteTask = DeleteTask(mockTaskRepo, mockAlarmInteractor)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        deleteTask(mockTask)
        coVerify { mockTaskRepo.deleteTask(mockTask) }
    }

    @Test
    fun `check if alarm was canceled`() = runBlockingTest {
        deleteTask(mockTask)
        verify { mockAlarmInteractor.cancel(mockTask.id) }
    }
}
