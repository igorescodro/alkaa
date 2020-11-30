package com.escodro.alkaa

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasSubstring
import androidx.compose.ui.test.onNodeWithText
import androidx.ui.test.createComposeRule
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.list.TaskItem
import com.escodro.theme.AlkaaTheme
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

internal class TaskItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_assertIfBasicTaskIsShown() {
        // Given a simple task
        val taskName = "Buy milk"
        val task = Task(title = taskName, dueDate = null)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the text is shown
        composeTestRule.onNodeWithText(taskName).assertExists()
    }

    @Test
    fun test_assertIfDueDateIsShown() {
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
        composeTestRule.onNode(hasSubstring(hour)).assertExists()
        composeTestRule.onNode(hasSubstring(minute)).assertExists()
    }

    private fun loadItemView(item: TaskWithCategory, onItemClicked: (TaskWithCategory) -> Unit) {
        composeTestRule.setContent {
            AlkaaTheme {
                TaskItem(Modifier, item, onItemClicked)
            }
        }
    }
}
