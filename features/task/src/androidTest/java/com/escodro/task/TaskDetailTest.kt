package com.escodro.task

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.model.Task
import com.escodro.task.presentation.detail.TaskDetailActions
import com.escodro.task.presentation.detail.main.TaskDetailRouter
import com.escodro.task.presentation.detail.main.TaskDetailState
import org.junit.Rule
import org.junit.Test

internal class TaskDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_errorViewIsShown() {
        // Given an error state
        val state = TaskDetailState.Error

        // When the view is loaded
        loadTaskDetail(state)

        // Assert that the error view is loaded
        val header = context.getString(R.string.task_detail_header_error)
        val contentDescription = context.getString(R.string.task_detail_cd_error)
        composeTestRule.onNodeWithText(text = header).assertExists()
        composeTestRule.onNodeWithContentDescription(label = contentDescription)
    }

    @Test
    fun test_detailContentIsShown() {
        // Given a success state
        val task = Task(
            title = "Call John",
            description = "I can't forget his birthday again",
            dueDate = null,
            categoryId = 10L
        )
        val state = TaskDetailState.Loaded(task)

        // When the view is loaded
        loadTaskDetail(state)

        // Assert that the task content is shown
        composeTestRule.onNodeWithText(text = task.title).assertExists()
        composeTestRule.onNodeWithText(text = task.description!!).assertExists()
    }

    private fun loadTaskDetail(state: TaskDetailState) {
        composeTestRule.setContent {
            AlkaaTheme {
                TaskDetailRouter(
                    detailViewState = state,
                    categoryViewState = CategoryState.Loaded(listOf()),
                    actions = TaskDetailActions()
                )
            }
        }
    }
}
