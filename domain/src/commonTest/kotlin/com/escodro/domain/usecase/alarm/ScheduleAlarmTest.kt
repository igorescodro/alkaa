package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.implementation.ScheduleAlarmImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import java.util.Calendar
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ScheduleAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    private val scheduleAlarmUseCase = ScheduleAlarmImpl(taskRepository, alarmInteractor)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
    }

    @Test
    fun `test if alarm is scheduled`() = runTest {
        val task = Task(id = 1, title = "I need a alarm here")
        val alarm = Calendar.getInstance()
        addTaskUseCase(task)

        scheduleAlarmUseCase(task.id, alarm)
        val result = getTaskUseCase(task.id)
        val assertTask = task.copy(dueDate = alarm)

        assertEquals(assertTask, result)
    }
}
