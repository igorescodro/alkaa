package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
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
class CancelAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val cancelAlarmUseCase = CancelAlarm(taskRepository, alarmInteractor)

    private val addTaskUseCase = AddTask(taskRepository)

    private val getTaskUseCase = GetTask(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
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

        val resultTask = getTaskUseCase(task.id).first()

        Assert.assertNull(resultTask.dueDate)
    }
}
