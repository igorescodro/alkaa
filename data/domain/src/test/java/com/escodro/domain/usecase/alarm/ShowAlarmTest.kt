package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ShowAlarmTest {

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val mockInteractor = mockk<NotificationInteractor>(relaxed = true)

    private val showAlarm = ShowAlarm(mockTaskRepo, mockInteractor)

    @Test
    fun `show alarm when task is not completed`() = runBlockingTest {
        val task = Task(1, title = "should show", completed = false)
        coEvery { mockTaskRepo.findTaskById(any()) } returns task
        showAlarm(task.id)

        verify { mockInteractor.show(task) }
    }

    @Test
    fun `ignore alarm when task is completed`() = runBlockingTest {
        val task = Task(1, title = "should show", completed = true)
        coEvery { mockTaskRepo.findTaskById(any()) } returns task
        showAlarm(task.id)

        verify(exactly = 0) { mockInteractor.show(task) }
    }
}
