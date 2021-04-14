package com.escodro.task

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.list.CategoryStateHandler
import com.escodro.task.presentation.list.TaskListScaffold
import com.escodro.task.presentation.list.TaskListViewState
import com.escodro.task.presentation.list.TaskStateHandler
import org.junit.Rule
import org.junit.Test

internal class TaskListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_errorViewIsShown() {
        // Given an error state
        val state = TaskListViewState.Error(IllegalAccessException())

        // When the view is loaded
        loadTaskList(state)

        // Assert that the error view is loaded
        val header = context.getString(R.string.task_list_header_error)
        val contentDescription = context.getString(R.string.task_list_cd_error)
        composeTestRule.onNodeWithText(text = header).assertExists()
        composeTestRule.onNodeWithContentDescription(label = contentDescription)
    }

    @Test
    fun test_emptyViewIsShown() {
        // Given an empty state
        val state = TaskListViewState.Empty

        // When the view is loaded
        loadTaskList(state)

        // Assert that the empty view is loaded
        val header = context.getString(R.string.task_list_header_empty)
        val contentDescription = context.getString(R.string.task_list_cd_empty_list)
        composeTestRule.onNodeWithText(text = header).assertExists()
        composeTestRule.onNodeWithContentDescription(label = contentDescription)
    }

    @Test
    fun test_listViewIsShown() {
        // Given a success state
        val task = Task(title = "Buy milk", dueDate = null)
        val category = Category(name = "Books", color = android.graphics.Color.GREEN)
        val taskList = listOf(TaskWithCategory(task = task, category = category))
        val state = TaskListViewState.Loaded(taskList)

        // When the view is loaded
        loadTaskList(state)

        // Assert that the item is shown on the list
        composeTestRule.onNodeWithText(text = task.title, useUnmergedTree = true).assertExists()
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun loadTaskList(state: TaskListViewState) {
        composeTestRule.setContent {
            AlkaaTheme {
                TaskListScaffold(
                    taskHandler = TaskStateHandler(state = state),
                    categoryHandler = CategoryStateHandler(),
                    modifier = Modifier,
                )
            }
        }
    }
}
