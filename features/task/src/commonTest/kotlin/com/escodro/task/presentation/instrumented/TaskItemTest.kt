package com.escodro.task.presentation.instrumented

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.list.TaskItem
import com.escodro.task.provider.RelativeDateTimeProvider
import com.escodro.test.AlkaaTest
import kotlinx.datetime.LocalDateTime
import org.koin.compose.KoinApplication
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskItemTest : AlkaaTest() {

    private val testModule = module {
        factory {
            object : RelativeDateTimeProvider {
                override fun toRelativeDateTimeString(dateTime: LocalDateTime): String =
                    "${dateTime.hour}:${dateTime.minute}"
            }
        } bind RelativeDateTimeProvider::class
    }

    @Test
    fun test_basicTaskIsShown() = runComposeUiTest {
        // Given a simple task
        val taskName = "Buy milk"
        val task = Task(title = taskName, dueDate = null)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the text is shown
        onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
    }

    @Test
    fun test_dueDateIsShown() = runComposeUiTest {
        // Given a task with due date to 1993-04-15
        val taskName = "Watch movies"
        val dueDate = LocalDateTime(year = 1993, month = 4, day = 15, hour = 12, minute = 0)
        val task = Task(title = taskName, dueDate = dueDate)
        val taskWithCategory = TaskWithCategory(task)

        // When the view is loaded
        loadItemView(taskWithCategory) {}

        // Assert that the due date time is shown
        val hour = dueDate.hour.toString()
        val minute = dueDate.minute.toString()
        onNodeWithText(text = hour, substring = true, useUnmergedTree = true)
            .assertExists()
        onNodeWithText(text = minute, substring = true, useUnmergedTree = true)
            .assertExists()
    }

    private fun ComposeUiTest.loadItemView(item: TaskWithCategory, onItemClick: (Long) -> Unit) {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                AlkaaThemePreview {
                    TaskItem(
                        task = item,
                        modifier = Modifier,
                        onItemClick = onItemClick,
                        onCheckedChange = {},
                    )
                }
            }
        }
    }
}
