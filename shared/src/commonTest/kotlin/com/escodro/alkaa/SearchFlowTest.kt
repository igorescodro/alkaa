package com.escodro.alkaa

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.compose.ui.test.waitUntilDoesNotExist
import com.escodro.alkaa.fake.FAKE_TASKS
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.local.dao.TaskDao
import com.escodro.task.presentation.list.CheckboxNameKey
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class SearchFlowTest : AlkaaTest(), KoinTest {

    private val taskDao: TaskDao by inject()

    @BeforeTest
    fun setup() {
        beforeTest()
        runTest {
            // Clean all existing tasks
            taskDao.cleanTable()

            // Add some fake tasks
            FAKE_TASKS.forEach { task -> taskDao.insertTask(task) }
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun when_no_query_then_all_tasks_are_visible() = uiTest {
        navigateToSearch()

        // Without typing anything on query text field

        FAKE_TASKS.forEach { task ->
            // Validate all tasks are shown
            onNodeWithText(text = task.task_title, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun when_query_then_only_matching_tasks_are_visible() = uiTest {
        navigateToSearch()

        // Type the first task as query and validate it is shown in the list
        val query = FAKE_TASKS.first().task_title
        onNode(hasSetTextAction()).performTextInput(query)
        onAllNodesWithText(text = query, useUnmergedTree = true)[1].assertExists()

        // Wait until the other second item is no longer visible
        waitUntilDoesNotExist(hasText(FAKE_TASKS[1].task_title))

        // Drop the first task and validate others are not shown
        FAKE_TASKS.drop(1).forEach { task ->
            // Validate all tasks are shown
            onNodeWithText(text = task.task_title, useUnmergedTree = true).assertDoesNotExist()
        }
    }

    @Test
    fun when_query_is_invalid_then_no_tasks_are_visible() = uiTest {
        navigateToSearch()

        onNode(hasSetTextAction()).performTextInput("query")

        // Wait until the first task is not visible
        waitUntilDoesNotExist(hasText(FAKE_TASKS[0].task_title))

        FAKE_TASKS.forEach { task ->
            // Validate all tasks are shown
            onNodeWithText(text = task.task_title, useUnmergedTree = true).assertDoesNotExist()
        }

        onNodeWithContentDescription(label = "No tasks").assertExists()
        onNodeWithText(text = "No tasks found").assertExists()
    }

    @Test
    fun clear_search_query() = uiTest {
        navigateToSearch()

        // Type a query
        val query = FAKE_TASKS.first().task_title
        onNode(hasSetTextAction()).performTextInput(query)
        onAllNodesWithText(text = query, useUnmergedTree = true)[1].assertExists()

        // Clear the query
        onNode(hasSetTextAction()).performTextReplacement("")

        // Validate all tasks are visible again
        waitUntilAtLeastOneExists(
            matcher = hasText(FAKE_TASKS.first().task_title),
            timeoutMillis = 5_000L,
        )
        FAKE_TASKS.forEach { task ->
            onNodeWithText(text = task.task_title, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun when_task_is_completed_it_shows_in_search_tab() = uiTest {
        val taskName = FAKE_TASKS.first().task_title

        // Complete the task in Tasks tab
        onNode(SemanticsMatcher.expectValue(CheckboxNameKey, taskName)).performClick()
        waitUntilDoesNotExist(hasText(taskName))

        // Navigate to Search tab and validate it is there
        navigateToSearch()
        onNode(hasSetTextAction()).performTextInput(taskName)
        onAllNodesWithText(text = taskName, useUnmergedTree = true).onLast().assertExists()
    }

    private fun ComposeUiTest.navigateToSearch() {
        onNodeWithContentDescription(label = "Search", useUnmergedTree = true).performClick()
    }
}
