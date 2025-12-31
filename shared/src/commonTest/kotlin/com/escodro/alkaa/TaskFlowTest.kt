package com.escodro.alkaa

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.compose.ui.test.waitUntilDoesNotExist
import androidx.compose.ui.test.waitUntilExactlyOneExists
import com.escodro.alkaa.fake.CoroutinesDebouncerFake
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.local.Category
import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.list.CheckboxNameKey
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.koin.core.component.inject
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskFlowTest : AlkaaTest(), KoinTest {

    private val taskDao: TaskDao by inject()

    private val categoryDao: CategoryDao by inject()

    @BeforeTest
    fun setup() {
        beforeTest()

        // Clean all existing tasks and categories
        runTest {
            taskDao.cleanTable()
            categoryDao.cleanTable()

            categoryDao.insertCategory(
                Category(
                    category_id = 11,
                    category_name = "Books",
                    category_color = "#cc5a71",
                ),
            )
            categoryDao.insertCategory(
                Category(
                    category_id = 12,
                    category_name = "Music",
                    category_color = "#58a4b0",
                ),
            )
            categoryDao.insertCategory(
                Category(
                    category_id = 13,
                    category_name = "Shared",
                    category_color = "#519872",
                ),
            )
        }

        // Replace Debouncer with a Immediate Executor
        declare<CoroutineDebouncer> { CoroutinesDebouncerFake() }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun add_and_open_task() = uiTest {
        // Add and open a task
        addAndOpenTask("Happy Hour (remote)")
    }

    @Test
    fun edit_task_name() = uiTest {
        addAndOpenTask("Watter planttes")

        // Edit the name of the task
        val newName = "Water plants"
        onAllNodes(hasSetTextAction())[0].performTextReplacement(newName)
        navigateBack()

        // Validate if the new name is shown
        waitUntilAtLeastOneExists(hasText(newName))
        onNodeWithText(text = newName, useUnmergedTree = true).assertExists()
    }

    @Test
    fun add_task_description() = uiTest {
        val taskName = "Listen to music"
        addAndOpenTask(taskName)

        // Add a description
        val description = "Phoebe Bridgers"
        onAllNodes(hasSetTextAction())[1].performTextReplacement(description)
        navigateBack()

        // Reopen the task and validate if the description is save
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        onNodeWithText(text = description, useUnmergedTree = true).assertExists()
    }

    @Test
    fun add_category_to_task() = uiTest {
        val taskName = "What the hell?"
        addAndOpenTask(taskName)

        // Select a category
        val category = "Music"
        onAllNodes(hasText(category)).onLast().performClick()
        navigateBack()

        // Reopen the task and validate if the category is selected
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        onAllNodes(hasText(category)).onLast().assertIsSelected()
    }

    @Test
    fun add_alarm_to_task() = uiTest {
        val taskName = "Wake wake! It's time for school!"
        addAndOpenTask(taskName)
        onNodeWithText("No alarm").performClick()

        onNodeWithText("Next").performClick()
        onNodeWithText("Confirm").performClick()
        navigateBack()

        // Reopen the task and validate if the alarm is on
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        onNodeWithText("No alarm").assertDoesNotExist()
    }

    @Test
    fun add_alarm_interval_to_task() = uiTest {
        val taskName = "Morning is here..."
        addAndOpenTask(taskName)
        onNodeWithText("No alarm").performClick()

        onNodeWithText("Next").performClick()
        onNodeWithText("Confirm").performClick()

        // Set repeating randomly
        val alarmArray = AlarmInterval.entries.map { runBlocking { getString(it.title) } }
        onNodeWithText(alarmArray[0]).performClick()
        onNodeWithText(alarmArray.last()).performClick()

        navigateBack()

        // Reopen the task and validate if the alarm is on
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        onNodeWithText(alarmArray[0]).assertDoesNotExist()
    }

    @Test
    fun add_alarm_via_bottom_sheet() = uiTest {
        val taskName = "Checkout my new video!"
        onNodeWithContentDescription("Add task").performClick()
        waitUntilExactlyOneExists(hasSetTextAction())
        onNode(hasSetTextAction()).performTextInput(taskName)

        // Add alarm
        onNodeWithText("No alarm").performClick()
        onNodeWithText("Next").performClick()
        onNodeWithText("Confirm").performClick()

        // Add alarm interval
        onNodeWithText("Never").performClick()
        onNodeWithText("Every day").performClick()
        onNodeWithText("Add").performClick()

        // Open and validate
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText("No alarm", useUnmergedTree = true).assertDoesNotExist()
        onNodeWithText("Every day", useUnmergedTree = true).assertExists()
    }

    @Test
    fun complete_task_from_list() = uiTest {
        val taskName = "Buy milk"
        onNodeWithContentDescription("Add task").performClick()
        waitUntilExactlyOneExists(hasSetTextAction())
        onNode(hasSetTextAction()).performTextInput(taskName)
        onNodeWithText("Add").performClick()

        // Click on the checkbox to complete the task
        onNode(SemanticsMatcher.expectValue(CheckboxNameKey, taskName)).performClick()

        // Validate if the task is no longer in the list
        waitUntilDoesNotExist(hasText(taskName))
    }

    @Test
    fun remove_alarm_from_task() = uiTest {
        val taskName = "Appreciate the coffee"
        addAndOpenTask(taskName)
        onNodeWithText("No alarm").performClick()

        onNodeWithText("Next").performClick()
        onNodeWithText("Confirm").performClick()

        // Remove the alarm
        onNodeWithContentDescription("Remove alarm").performClick()
        navigateBack()

        // Reopen the task and validate if the alarm is off
        waitUntilAtLeastOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        onNodeWithText("No alarm").assertExists()
    }

    @Test
    fun when_task_has_no_title_it_is_not_created() = uiTest {
        onNodeWithContentDescription("Add task").performClick()
        waitUntilExactlyOneExists(hasSetTextAction())

        // Click Add without typing anything
        onNodeWithText("Add").performClick()

        // Bottom sheet should be closed
        onNodeWithText("Add").assertDoesNotExist()

        // No task should be in the list
        onNodeWithText("Wow! All tasks are completed!").assertExists()
    }

    private fun ComposeUiTest.addAndOpenTask(taskName: String) {
        onNodeWithContentDescription("Add task").performClick()
        waitUntilExactlyOneExists(hasSetTextAction())
        onNode(hasSetTextAction()).performTextInput(taskName)
        onNodeWithText("Add").performClick()
        waitUntilExactlyOneExists(hasText(taskName))
        onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
        waitUntilAtLeastOneExists(hasText(taskName))
        onAllNodes(hasSetTextAction()).assertAny(hasText(taskName, substring = true))
    }

    /**
     * Force the navigation back to the previous screen based on the content description.
     */
    @Suppress("ForbiddenMethodCall")
    private fun ComposeUiTest.navigateBack() = try {
        onNodeWithContentDescription("Back", substring = true).performClick()
    } catch (e: AssertionError) {
        onNodeWithContentDescription("Close", substring = true).performClick()
        println(e.message)
    }
}
