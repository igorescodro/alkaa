package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.implementation.UpdateTaskAsRepeatingImpl
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class UpdateTaskAsRepeatingTest {

    private val taskRepository = TaskRepositoryFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val scheduleRepeatingUseCase = UpdateTaskAsRepeatingImpl(taskRepository)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
    }

    @Test
    fun `test if task is updated with new interval`() = runTest {
        val task = Task(id = 984L, title = "I'll be there for you")
        addTaskUseCase(task)
        val interval = AlarmInterval.WEEKLY

        scheduleRepeatingUseCase(task.id, interval)

        val result = getTaskUseCase(task.id)
        require(result != null)
        assertEquals(interval, result.alarmInterval)
        assertTrue(result.isRepeating)
    }

    @Test
    fun `test if repeating state is cleared when update alarm interval to never`() = runTest {
        val task = Task(
            id = 984L,
            title = "nanana",
            alarmInterval = AlarmInterval.YEARLY,
            isRepeating = true,
        )

        addTaskUseCase(task)

        scheduleRepeatingUseCase(task.id, null)

        val result = getTaskUseCase(task.id)
        require(result != null)
        assertEquals(null, result.alarmInterval)
        assertFalse(result.isRepeating)
    }
}
