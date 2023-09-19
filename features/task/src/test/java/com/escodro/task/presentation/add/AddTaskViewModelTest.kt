package com.escodro.task.presentation.add

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.fake.AddTaskFake
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddTaskViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val addTask = AddTaskFake()

    private val viewModel = AddTaskViewModel(
        addTaskUseCase = addTask,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
    )

    @Before
    fun setup() {
        addTask.clear()
    }

    @Test
    fun `test if when a task is created when function is called`() {
        val taskTitle = "Rendez-vous"

        viewModel.addTask(taskTitle, CategoryId(null))

        Assert.assertTrue(addTask.wasTaskCreated(taskTitle))
    }

    @Test
    fun `test if when a task is not created when title is empty`() {
        val taskTitle = ""

        viewModel.addTask(taskTitle, CategoryId(null))

        Assert.assertFalse(addTask.wasTaskCreated(taskTitle))
    }
}
