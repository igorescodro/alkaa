package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
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
import com.escodro.resources.category_details_section_completed
import com.escodro.resources.category_details_section_due_today
import com.escodro.resources.category_details_section_no_due_date
import com.escodro.resources.category_details_section_overdue
import com.escodro.resources.category_details_section_upcoming
import com.escodro.test.AlkaaTest
import kotlinx.collections.immutable.persistentListOf
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
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(),
                totalTasks = 0,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText(emptyTitle).assertIsDisplayed()
    }

    @Test
    fun test_taskGroupsAreShown() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Buy milk")

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.NoDueDate(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText("Buy milk").assertIsDisplayed()
    }

    @Test
    fun test_overdueSectionHeaderIsDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Overdue task")
        val sectionHeader = runBlocking { getString(Res.string.category_details_section_overdue) }

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.Overdue(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }

    @Test
    fun test_dueTodaySectionHeaderIsDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Due today task")
        val sectionHeader = runBlocking { getString(Res.string.category_details_section_due_today) }

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.DueToday(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }

    @Test
    fun test_upcomingSectionHeaderIsDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Upcoming task")
        val sectionHeader = runBlocking { getString(Res.string.category_details_section_upcoming) }

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.Upcoming(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }

    @Test
    fun test_noDueDateSectionHeaderIsDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "No due date task")
        val sectionHeader = runBlocking { getString(Res.string.category_details_section_no_due_date) }

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.NoDueDate(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 0,
            ),
        )

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }

    @Test
    fun test_completedSectionHeaderIsDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Completed task", isCompleted = true)
        val sectionHeader = runBlocking { getString(Res.string.category_details_section_completed) }

        // When
        setUpContent(
            CategoryDetailsData(
                category = testCategory,
                categoryColor = testColor,
                groups = persistentListOf(TaskGroup.Completed(tasks = listOf(task))),
                totalTasks = 1,
                completedTasks = 1,
            ),
        )

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }

    private fun ComposeUiTest.setUpContent(data: CategoryDetailsData) {
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    data = data,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                )
            }
        }
    }
}
