package com.escodro.task

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.list.TaskItem
import com.escodro.test.assertLines
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

internal class TaskItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_basicTaskIsShown() {
        // Given a simple task
        val taskName = "Buy milk"
        val task = Task(title = taskName, dueDate = null)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the text is shown
        composeTestRule.onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
    }

    @Test
    fun test_dueDateIsShown() {
        // Given a task wit due date to 1993-04-15
        val taskName = "Watch movies"
        val calendar = Calendar.getInstance().apply { timeInMillis = 734_904_000_000 }
        val task = Task(title = taskName, dueDate = calendar)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the due date time is shown
        val hour = calendar.get(Calendar.HOUR).toString()
        val minute = calendar.get(Calendar.MINUTE).toString()
        composeTestRule.onNodeWithText(text = hour, substring = true, useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithText(text = minute, substring = true, useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun test_taskTitleIsSingleLine() {
        // Given a task with long title
        val taskName = "This is a very long task title and should only fit one line. Nothing more.."
        val task = Task(title = taskName, dueDate = null)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the title has a single line
        composeTestRule.onNodeWithText(text = taskName, useUnmergedTree = true)
            .assertLines(lines = 1)
    }

    private fun loadItemView(item: TaskWithCategory, onItemClicked: (Long) -> Unit) {
        composeTestRule.setContent {
            AlkaaTheme {
                TaskItem(Modifier, item, onItemClicked, onCheckedChange = {})
            }
        }
    }
}
