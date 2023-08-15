package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.fake.CoroutinesDebouncerFake
import com.escodro.alkaa.model.HomeSection
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.util.WindowSizeClassFake
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.Category
import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.test.espresso.Events
import com.escodro.test.extension.onChip
import com.escodro.test.rule.DisableAnimationsRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import java.util.Calendar
import com.escodro.designsystem.R as DesignSystemR
import com.escodro.task.R as TaskR

@OptIn(ExperimentalTestApi::class)
internal class TaskFlowTest : KoinTest {

    private val taskDao: TaskDao by inject()

    private val categoryDao: CategoryDao by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
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

        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph(windowSizeClass = WindowSizeClassFake.Phone)
            }
        }
    }

    @Test
    fun test_addAndOpenTask() {
        // Add and open a task
        addAndOpenTask("Happy Hour (remote)")
    }

    @Test
    fun test_editTaskName() {
        addAndOpenTask("Watter planttes")

        with(composeTestRule) {
            // Edit the name of the task
            val newName = "Water plants"
            onAllNodes(hasSetTextAction())[0].performTextReplacement(newName)
            pressBack()

            // Validate if the new name is shown
            onNodeWithText(text = newName, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun test_addTaskDescription() {
        val taskName = "Listen to music"
        addAndOpenTask(taskName)

        with(composeTestRule) {
            // Add a description
            val description = "Phoebe Bridgers"
            onAllNodes(hasSetTextAction())[1].performTextReplacement(description)
            pressBack()

            // Reopen the task and validate if the description is save
            waitUntilAtLeastOneExists(hasText(taskName))
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onNodeWithText(text = description, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun test_selectCategory() {
        val taskName = "What the hell?"
        addAndOpenTask(taskName)

        with(composeTestRule) {
            // Select a category
            val category = "Music"
            onChip(category).performClick()
            pressBack()

            // Reopen the task and validate if the category is selected
            waitUntilAtLeastOneExists(hasText(taskName))
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onChip(category).assertIsSelected()
        }
    }

    @Test
    fun test_alarmIsSaved() {
        val taskName = "Wake wake! It's time for school!"
        addAndOpenTask(taskName)
        with(composeTestRule) {
            onNodeWithText(string(TaskR.string.task_detail_alarm_no_alarm)).performClick()

            // Set alarm to 2021-04-15 - 17:00:00
            val calendar = Calendar.getInstance().apply { timeInMillis = 1_650_042_000 }
            Events.setDateTime(calendar)
            pressBack()

            // Reopen the task and validate if the alarm is on
            waitUntilAtLeastOneExists(hasText(taskName))
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onNodeWithText(string(TaskR.string.task_detail_alarm_no_alarm)).assertDoesNotExist()
        }
    }

    @Test
    fun test_alarmIntervalIsSaved() {
        val taskName = "Morning is here..."
        addAndOpenTask(taskName)
        with(composeTestRule) {
            onNodeWithText(string(TaskR.string.task_detail_alarm_no_alarm)).performClick()

            // Set alarm to 2021-04-15 - 17:00:00
            val calendar = Calendar.getInstance().apply { timeInMillis = 1_650_042_000 }
            Events.setDateTime(calendar)

            // Set repeating randomly
            val alarmArray =
                context.resources.getStringArray(com.escodro.task.R.array.task_alarm_repeating)
            onNodeWithText(alarmArray[0]).performClick()
            onNodeWithText(alarmArray.last()).performClick()

            pressBack()

            // Reopen the task and validate if the alarm is on
            waitUntilAtLeastOneExists(hasText(taskName))
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onNodeWithText(alarmArray[0]).assertDoesNotExist()
        }
    }

    private fun addAndOpenTask(taskName: String) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                string(TaskR.string.task_cd_add_task),
                useUnmergedTree = true,
            ).performClick()
            onNode(hasSetTextAction()).performTextInput(taskName)
            onNodeWithText(string(TaskR.string.task_add_save)).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
        }
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)

    private fun pressBack() {
        composeTestRule.onNodeWithContentDescription(
            string(DesignSystemR.string.back_arrow_cd),
            useUnmergedTree = true,
        ).performClick()

        // Wait the list to be loaded
        val searchTitle = context.getString(HomeSection.Search.title)
        composeTestRule.waitUntilAtLeastOneExists(hasText(searchTitle))
    }
}
