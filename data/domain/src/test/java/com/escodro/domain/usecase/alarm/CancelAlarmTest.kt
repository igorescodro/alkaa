package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CancelAlarmTest {

    private val mockInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val cancelAlarm = CancelAlarm(mockTaskRepo, mockInteractor)

    @Test
    fun `check if interactor function was called`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        coEvery { mockTaskRepo.findTaskById(task.id) } returns task

        cancelAlarm(task.id)
        verify { mockInteractor.cancel(task.id) }
    }

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        coEvery { mockTaskRepo.findTaskById(task.id) } returns task

        cancelAlarm(task.id)
        coVerify { mockTaskRepo.findTaskById(task.id) }
    }

    @Test
    fun `check if the alarm is being removed from task`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        coEvery { mockTaskRepo.findTaskById(task.id) } returns task

        cancelAlarm(task.id)

        val assertTask = task.copy(dueDate = null)
        coVerify { mockTaskRepo.updateTask(assertTask) }
    }
}
