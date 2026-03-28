package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.resources.Res
import com.escodro.resources.category_details_empty_title
import com.escodro.resources.category_details_section_no_due_date
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class CategoryDetailsScreenTest : AlkaaTest() {

    private val testCategory = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt())
    private val testColor = Color(0xFF6200EA)

    @Test
    fun test_emptyStateIsShownWhenNoTasks() = runComposeUiTest {
        // Given
        val emptyTitle = runBlocking { getString(Res.string.category_details_empty_title) }

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = emptyList(),
                    totalTasks = 0,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText(emptyTitle).assertIsDisplayed()
    }

    @Test
    fun test_taskGroupsAreShown() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Buy milk")
        val groups = listOf(TaskGroup.NoDueDate(tasks = listOf(task)))

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = groups,
                    totalTasks = 1,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText("Buy milk").assertIsDisplayed()
    }

    @Test
    fun test_correctSectionHeadersAreDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Task 1")
        val groups = listOf(TaskGroup.NoDueDate(tasks = listOf(task)))
        val sectionHeader = runBlocking {
            getString(Res.string.category_details_section_no_due_date)
        }

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = groups,
                    totalTasks = 1,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }
}
