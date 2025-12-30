package com.escodro.task.presentation.add

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.fake.AddTaskFake
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AddTaskViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val addTask = AddTaskFake()

    private val alarmIntervalMapper = AlarmIntervalMapper()

    private val viewModel = AddTaskViewModel(
        addTaskUseCase = addTask,
        alarmIntervalMapper = alarmIntervalMapper,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
    )

    @BeforeTest
    fun setup() {
        addTask.clear()
    }

    @Test
    fun `test if when a task is created when function is called`() {
        val taskTitle = "Rendez-vous"

        viewModel.addTask(
            title = taskTitle,
            categoryId = CategoryId(null),
            dueDate = null,
            alarmInterval = AlarmInterval.NEVER,
        )

        assertEquals(expected = taskTitle, actual = addTask.createdTask?.title)
    }

    @Test
    fun `test if when a task is not created when title is empty`() {
        val taskTitle = ""

        viewModel.addTask(
            title = taskTitle,
            categoryId = CategoryId(value = null),
            dueDate = null,
            alarmInterval = AlarmInterval.MONTHLY,
        )

        assertNull(this.addTask.createdTask)
    }

    @Test
    fun `test if due date is created in the task`() {
        val taskTitle = "Voulez-vous?"
        val dueDate = LocalDateTime(year = 2022, month = 1, day = 1, hour = 12, minute = 0)

        viewModel.addTask(
            title = taskTitle,
            categoryId = CategoryId(null),
            dueDate = dueDate,
            alarmInterval = AlarmInterval.NEVER,
        )

        assertEquals(expected = taskTitle, actual = addTask.createdTask?.title)
        assertEquals(expected = dueDate, actual = addTask.createdTask?.dueDate)
    }

    @Test
    fun `test if alarm interval is created in the task`() {
        val taskTitle = "Coucher avec moi?"
        val alarmInterval = AlarmInterval.WEEKLY

        viewModel.addTask(
            title = taskTitle,
            categoryId = CategoryId(null),
            dueDate = null,
            alarmInterval = alarmInterval,
        )

        val assertAlarmInterval = alarmIntervalMapper.toDomain(alarmInterval)

        assertEquals(expected = taskTitle, actual = addTask.createdTask?.title)
        assertEquals(expected = assertAlarmInterval, actual = addTask.createdTask?.alarmInterval)
    }
}
