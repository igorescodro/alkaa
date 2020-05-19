package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.GetTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UpdateTaskAsRepeatingTest {

    private val taskRepository = TaskRepositoryFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val getTaskUseCase = GetTask(taskRepository)

    private val scheduleRepeatingUseCase = UpdateTaskAsRepeating(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
    }

    @Test
    fun `test if task is updated with new interval`() = runBlockingTest {
        val task = Task(id = 984L, title = "I'll be there for you")
        addTaskUseCase(task)
        val interval = AlarmInterval.WEEKLY

        scheduleRepeatingUseCase(task.id, interval)

        val result = getTaskUseCase(task.id).first()
        Assert.assertEquals(interval, result.alarmInterval)
        Assert.assertTrue(result.isRepeating)
    }

    @Test
    fun `test if repeating state is cleared when update alarm interval to never`() =
        runBlockingTest {
            val task = Task(
                id = 984L,
                title = "nanana",
                alarmInterval = AlarmInterval.YEARLY,
                isRepeating = true
            )

            addTaskUseCase(task)

            scheduleRepeatingUseCase(task.id, null)

            val result = getTaskUseCase(task.id).first()
            Assert.assertEquals(null, result.alarmInterval)
            Assert.assertFalse(result.isRepeating)
        }
}
