package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.implementation.CancelAlarmImpl
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.UpdateAlarmFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull

class CancelAlarmTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val updateAlarm = UpdateAlarmFake()

    private val cancelAlarmUseCase = CancelAlarmImpl(taskRepository, alarmInteractor)

    private val addTaskUseCase = AddTaskImpl(taskRepository, updateAlarm, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
    }

    @Test
    fun test_if_the_alarm_is_canceled() = runTest {
        val task = Task(id = 11, title = "reminder!")
        addTaskUseCase(task)
        cancelAlarmUseCase(task.id)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun test_if_alarm_is_removed_from_task() = runTest {
        val task = Task(id = 66, title = "tik tok on the clock")
        addTaskUseCase(task)
        cancelAlarmUseCase(task.id)

        val result = getTaskUseCase(task.id)

        requireNotNull(result)
        assertNull(result.dueDate)
    }
}
