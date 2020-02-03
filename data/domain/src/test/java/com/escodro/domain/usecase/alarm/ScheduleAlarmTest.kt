package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ScheduleAlarmTest {

    private val mockInteractor = mockk<AlarmInteractor>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val scheduleAlarm = ScheduleAlarm(mockTaskRepo, mockInteractor)

    @Test
    fun `check if interactor function was called`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        val time = Calendar.getInstance()

        coEvery { mockTaskRepo.findTaskById(task.id) } returns task
        scheduleAlarm(task.id, time)

        verify { mockInteractor.schedule(task.id, time.time.time) }
    }

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        val time = Calendar.getInstance()

        coEvery { mockTaskRepo.findTaskById(task.id) } returns task
        scheduleAlarm(task.id, time)

        coVerify { mockTaskRepo.findTaskById(task.id) }
    }

    @Test
    fun `check if task was updated with new alarm`() = runBlockingTest {
        val task = mockk<Task>(relaxed = true)
        val time = Calendar.getInstance()

        coEvery { mockTaskRepo.findTaskById(task.id) } returns task
        scheduleAlarm(task.id, time)

        val assertTask = task.copy(dueDate = time)
        coVerify { mockTaskRepo.updateTask(assertTask) }
    }
}
