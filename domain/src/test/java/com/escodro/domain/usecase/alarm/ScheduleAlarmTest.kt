package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.implementation.ScheduleAlarmImpl
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
import java.util.Calendar

@ExperimentalCoroutinesApi
internal class ScheduleAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val scheduleAlarmUseCase = ScheduleAlarmImpl(taskRepository, alarmInteractor)

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
        val result = getTaskUseCase(task.id)
        val assertTask = task.copy(dueDate = alarm)

        Assert.assertEquals(assertTask, result)
    }
}
