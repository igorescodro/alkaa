package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.GetTask
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ScheduleAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val getTaskUseCase = GetTask(taskRepository)

    private val scheduleAlarmUseCase = ScheduleAlarm(taskRepository, alarmInteractor)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun `test if alarm is scheduled`() = runBlockingTest {
        val task = Task(id = 1, title = "I need a alarm here")
        val alarm = Calendar.getInstance()
        addTaskUseCase(task)

        scheduleAlarmUseCase(task.id, alarm)
        val result = getTaskUseCase(task.id).first()
        val assertTask = task.copy(dueDate = alarm)

        Assert.assertEquals(assertTask, result)
    }
}
