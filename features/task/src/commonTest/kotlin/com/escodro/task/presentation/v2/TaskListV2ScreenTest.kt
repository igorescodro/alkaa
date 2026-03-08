package com.escodro.task.presentation.v2

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.test.AlkaaTest
import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskListV2ScreenTest : AlkaaTest() {

    @Test
    fun loading_state_shows_loading_indicator() = runComposeUiTest {
        loadScaffold(TaskListV2ViewState.Loading)
        onNodeWithText("Work", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun error_state_shows_error_message() = runComposeUiTest {
        loadScaffold(TaskListV2ViewState.Error(Exception("test")))
        onNodeWithText("Could not load this list", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun empty_loaded_state_shows_empty_message() = runComposeUiTest {
        loadScaffold(
            TaskListV2ViewState.Loaded(
                categoryName = "Work",
                categoryEmoji = "📋",
                totalCount = 0,
                completedCount = 0,
                sections = persistentListOf(),
                addTaskText = "",
            ),
        )
        onNodeWithText("No tasks yet. Add your first one below!", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun loaded_state_shows_category_header() = runComposeUiTest {
        val task = TaskItem(id = 1, title = "Test task", isCompleted = false, dueDate = null)
        loadScaffold(
            TaskListV2ViewState.Loaded(
                categoryName = "Work",
                categoryEmoji = "📋",
                totalCount = 3,
                completedCount = 1,
                sections = persistentListOf(
                    TaskSection(
                        type = TaskSectionType.NO_DATE,
                        tasks = persistentListOf(task),
                    ),
                ),
                addTaskText = "",
            ),
        )
        onNodeWithText("Work", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText("3 tasks · 1 completed", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun loaded_state_shows_section_headers() = runComposeUiTest {
        val task = TaskItem(id = 1, title = "Test task", isCompleted = false, dueDate = null)
        loadScaffold(
            TaskListV2ViewState.Loaded(
                categoryName = "Work",
                categoryEmoji = "📋",
                totalCount = 1,
                completedCount = 0,
                sections = persistentListOf(
                    TaskSection(
                        type = TaskSectionType.NO_DATE,
                        tasks = persistentListOf(task),
                    ),
                ),
                addTaskText = "",
            ),
        )
        onNodeWithText("No date", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText("Test task", useUnmergedTree = true).assertIsDisplayed()
    }

    private fun ComposeUiTest.loadScaffold(state: TaskListV2ViewState) {
        setContent {
            AlkaaThemePreview {
                TaskListV2Scaffold(
                    state = state,
                    onBack = {},
                    onTaskClick = {},
                    onAddTaskTextChange = {},
                    onAddTaskSubmit = {},
                    onTaskCheckedChange = {},
                )
            }
        }
    }
}
