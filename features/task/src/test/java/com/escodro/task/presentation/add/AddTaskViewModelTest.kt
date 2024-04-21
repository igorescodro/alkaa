package com.escodro.task.presentation.add

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.fake.AddTaskFake
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.datetime.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddTaskViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val addTask = AddTaskFake()

    private val alarmIntervalMapper = AlarmIntervalMapper()

    private val viewModel = AddTaskViewModel(
        addTaskUseCase = addTask,
        alarmIntervalMapper = alarmIntervalMapper,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
    )

    @Before
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

        Assert.assertEquals(taskTitle, addTask.createdTask?.title)
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

        Assert.assertTrue(addTask.createdTask == null)
    }

    @Test
    fun `test if due date is created in the task`() {
        val taskTitle = "Voulez-vous?"
        val dueDate = LocalDateTime(2022, 1, 1, 12, 0)

        viewModel.addTask(
            title = taskTitle,
            categoryId = CategoryId(null),
            dueDate = dueDate,
            alarmInterval = AlarmInterval.NEVER,
        )

        Assert.assertEquals(taskTitle, addTask.createdTask?.title)
        Assert.assertEquals(dueDate, addTask.createdTask?.dueDate)
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

        Assert.assertEquals(taskTitle, addTask.createdTask?.title)
        Assert.assertEquals(assertAlarmInterval, addTask.createdTask?.alarmInterval)
    }
}
