package com.escodro.task.presentation.instrumented

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.task_list_cd_empty_list
import com.escodro.resources.task_list_cd_error
import com.escodro.resources.task_list_header_empty
import com.escodro.resources.task_list_header_error
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.list.TaskListScaffold
import com.escodro.task.presentation.list.TaskListViewState
import com.escodro.test.AlkaaTest
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskListTest : AlkaaTest() {

    @Test
    fun test_errorViewIsShown() = runComposeUiTest {
        // Given an error state
        val state = TaskListViewState.Error(IllegalStateException())

        // When the view is loaded
        loadTaskList(state)

        // Assert that the error view is loaded
        val header = runBlocking { getString(Res.string.task_list_header_error) }
        val contentDescription = runBlocking { getString(Res.string.task_list_cd_error) }
        onNodeWithText(text = header).assertExists()
        onNodeWithContentDescription(label = contentDescription).assertExists()
    }

    @Test
    fun test_emptyViewIsShown() = runComposeUiTest {
        // Given an empty state
        val state = TaskListViewState.Empty

        // When the view is loaded
        loadTaskList(state)

        // Assert that the empty view is loaded
        val header = runBlocking { getString(Res.string.task_list_header_empty) }
        val contentDescription = runBlocking { getString(Res.string.task_list_cd_empty_list) }
        onNodeWithText(text = header).assertExists()
        onNodeWithContentDescription(label = contentDescription).assertExists()
    }

    @Test
    fun test_listViewIsShown() = runComposeUiTest {
        // Given a success state
        val task = Task(title = "Buy milk", dueDate = null)
        val category = Category(name = "Books", color = Color.Green.hashCode())
        val taskList = listOf(TaskWithCategory(task = task, category = category))
        val state = TaskListViewState.Loaded(taskList.toImmutableList())

        // When the view is loaded
        loadTaskList(state)

        // Assert that the item is shown on the list
        onNodeWithText(text = task.title, useUnmergedTree = true).assertExists()
    }

    private fun ComposeUiTest.loadTaskList(state: TaskListViewState) {
        setContent {
            AlkaaThemePreview {
                TaskListScaffold(
                    taskViewState = state,
                    categoryViewState = CategoryState.Loaded(persistentListOf()),
                    onFabClick = {},
                    onTaskCheckedChange = {},
                    onItemClick = {},
                    currentCategory = null,
                    onCategoryChange = {},
                    refreshKey = 0,
                    modifier = Modifier,
                )
            }
        }
    }
}
