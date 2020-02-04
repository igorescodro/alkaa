package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateTaskAsRepeatingTest {

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val scheduleRepeating = UpdateTaskAsRepeating(mockTaskRepo)

    @Test
    fun `check if task is updated with new alarm interval`() = runBlockingTest {
        val interval = AlarmInterval.DAILY
        val task = mockk<Task>(relaxed = true)

        coEvery { mockTaskRepo.findTaskById(task.id) } returns task

        scheduleRepeating(task.id, interval)

        val assertTask = task.copy(alarmInterval = interval, isRepeating = true)
        coVerify { mockTaskRepo.updateTask(assertTask) }
    }

    @Test
    fun `check if repeating is cleared if alarm interval is never`() = runBlockingTest {
        val interval = null
        val task = mockk<Task>(relaxed = true)

        coEvery { mockTaskRepo.findTaskById(task.id) } returns task

        scheduleRepeating(task.id, interval)

        val assertTask = task.copy(alarmInterval = null, isRepeating = false)
        coVerify { mockTaskRepo.updateTask(assertTask) }
    }
}
