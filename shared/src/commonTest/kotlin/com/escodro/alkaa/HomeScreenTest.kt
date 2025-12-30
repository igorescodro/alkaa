package com.escodro.alkaa

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.compose.ui.test.waitUntilExactlyOneExists
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.local.dao.TaskDao
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TopLevelDestinations
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest : AlkaaTest(), KoinTest {

    private val taskDao: TaskDao by inject()

    @BeforeTest
    fun setup() {
        beforeTest()
        runTest {
            taskDao.cleanTable()
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun when_tab_changes_then_title_updates() = uiTest {
        TopLevelDestinations.forEach { section ->
            val title = runBlocking { getString(section.title) }

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onNodeWithTag(section.title.toString()).assertIsSelected()
        }
    }

    @Test
    fun when_tab_changes_and_back_pressed_then_title_updates() = uiTest {
        // Click on Settings tab
        val settingsTitle = runBlocking { getString(HomeDestination.Preferences.title) }
        onNodeWithContentDescription(label = settingsTitle, useUnmergedTree = true).performClick()
        onNodeWithTag(HomeDestination.Preferences.title.toString()).assertIsSelected()

        // Press back button
        onAllNodes(isRoot())[0].performKeyInput {
            keyDown(Key.Back)
            keyUp(Key.Back)
        }

        // Click on Tasks tab
        val tasksTitle = runBlocking { getString(HomeDestination.TaskList.title) }
        onNodeWithContentDescription(label = tasksTitle, useUnmergedTree = true).performClick()
        onNodeWithTag(HomeDestination.TaskList.title.toString()).assertIsSelected()
    }

    @Test
    fun when_in_detail_screen_and_tab_clicked_again_then_it_returns_to_home() = uiTest {
        // Add a task to be able to open it
        val tasksTitle = runBlocking { getString(HomeDestination.TaskList.title) }
        val taskName = "Task for navigation"
        onNodeWithContentDescription("Add task").performClick()
        waitUntilExactlyOneExists(hasSetTextAction())
        onNode(hasSetTextAction()).performTextInput(taskName)
        onNodeWithText("Add").performClick()

        // Open the task detail
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(taskName).performClick()
        onNodeWithContentDescription("Description").assertExists()

        // Click on Tasks tab again
        onAllNodes(hasText(tasksTitle)).onLast().performClick()

        // Validate if it is back to the list
        onNodeWithContentDescription("Add task").assertExists()
    }

    @Test
    fun when_in_search_detail_screen_and_tab_clicked_again_then_it_returns_to_search_home() =
        uiTest {
            // Navigate to Search tab
            val searchTitle = runBlocking { getString(HomeDestination.Search.title) }
            onNodeWithContentDescription(label = searchTitle, useUnmergedTree = true).performClick()

            // Add a task to be able to search and open it
            val taskName = "Search navigation task"
            runTest {
                taskDao.insertTask(
                    com.escodro.local.Task(
                        task_id = 12_345,
                        task_is_completed = false,
                        task_title = taskName,
                        task_description = null,
                        task_category_id = null,
                        task_due_date = null,
                        task_creation_date = null,
                        task_completed_date = null,
                        task_is_repeating = false,
                        task_alarm_interval = null,
                    ),
                )
            }

            // Search and open the task detail
            onNode(hasSetTextAction()).performTextInput(taskName)
            waitUntilAtLeastOneExists(hasText(taskName))
            onAllNodesWithText(taskName).onLast().performClick()
            onNodeWithContentDescription("Description").assertExists()

            // Click on Search tab again
            onAllNodes(hasText(searchTitle)).onLast().performClick()

            // Validate if it is back to the search home
            onNodeWithTag("search_bar").assertExists()
        }

    @Test
    fun when_in_more_detail_screen_and_tab_clicked_again_then_it_returns_to_more_home() = uiTest {
        // Navigate to More tab
        val moreTitle = runBlocking { getString(HomeDestination.Preferences.title) }
        onNodeWithContentDescription(label = moreTitle, useUnmergedTree = true).performClick()

        // Open About screen
        val aboutTitle = "About"
        onNodeWithText(aboutTitle).performClick()
        onNodeWithText("alkaa").assertExists()

        // Click on More tab again
        onAllNodes(hasText(moreTitle)).onLast().performClick()

        // Validate if it is back to the More home
        onNodeWithText(aboutTitle).assertExists()
        onNodeWithText("Version").assertExists()
    }
}
