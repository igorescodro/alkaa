package com.escodro.task.presentation.add

import com.escodro.core.coroutines.AppCoroutineScope
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.fake.AddTaskFake
import com.escodro.test.rule.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddTaskViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val addTask = AddTaskFake()

    private val viewModel = AddTaskViewModel(
        addTaskUseCase = addTask,
        applicationScope = AppCoroutineScope(context = coroutineTestRule.testDispatcher),
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
