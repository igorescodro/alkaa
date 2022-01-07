package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.implementation.CancelAlarmImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CancelAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val cancelAlarmUseCase = CancelAlarmImpl(taskRepository, alarmInteractor)

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
    }

    @Test
    fun `test if the alarm is canceled`() = runBlockingTest {
        val task = Task(id = 11, title = "reminder!")
        addTaskUseCase(task)
        cancelAlarmUseCase(task.id)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if alarm is removed from task`() = runBlockingTest {
        val task = Task(id = 66, title = "tik tok on the clock")
        addTaskUseCase(task)
        cancelAlarmUseCase(task.id)

        val result = getTaskUseCase(task.id)

        require(result != null)
        Assert.assertNull(result.dueDate)
    }
}
