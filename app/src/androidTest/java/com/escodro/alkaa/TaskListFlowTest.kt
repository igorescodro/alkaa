package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.model.Category
import com.escodro.local.provider.DaoProvider
import com.escodro.task.presentation.list.CheckboxNameKey
import com.escodro.test.FlakyTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import com.escodro.task.R as TaskR

internal class TaskListFlowTest : FlakyTest(), KoinTest {

    private val daoProvider: DaoProvider by inject()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        // Clean all existing tasks and categories
        runBlocking {
            with(daoProvider) {
                getTaskDao().cleanTable()
                getCategoryDao().cleanTable()

                getCategoryDao().insertCategory(Category(name = "Books", color = "#cc5a71"))
                getCategoryDao().insertCategory(Category(name = "Music", color = "#58a4b0"))
                getCategoryDao().insertCategory(Category(name = "Work", color = "#519872"))
            }
        }
        setContent {
            AlkaaTheme {
                NavGraph()
            }
        }
    }

    @Test
    fun test_addTask() {
        // Create a simple task and validate if it is visible in the list
        addTask("Buy Pokémon cards")
    }

    @Test
    fun test_completeTask() {
        // Create a simple task and validate if it is visible in the list
        val taskName = "Listen to Taylor Swift"
        addTask(taskName)
        with(composeTestRule) {
            // Validate if when clicking on the checkbox makes it complete and disappears
            onCheckbox(taskName).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertDoesNotExist()
        }
    }

    @Test
    fun test_taskWithoutTitle() {
        with(composeTestRule) {
            // Try to insert a task without title
            val taskName = ""
            clickAddTask()
            onNode(hasSetTextAction()).performTextInput(taskName)
            onNodeWithText(string(TaskR.string.task_add_save)).performClick()

            // Validate if task without title is not created
            onCheckbox(taskName).assertDoesNotExist()
        }
    }

    @Test
    fun test_categoryFilter() {
        val taskName = "Meeting with Rodney"
        with(composeTestRule) {
            // Create a task in Work category and validate if it is visible in the list
            clickAddTask()
            onAllNodesWithText("Work")[1].performClick()
            onNode(hasSetTextAction()).performTextInput(taskName)
            onNodeWithText(string(TaskR.string.task_add_save)).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()

            // Click in the Work filter and validate if still visible
            onAllNodesWithText("Work")[0].performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()

            // Click in the Shopping List filter and validate if task is no longer visible
            onAllNodesWithText("Music")[0].performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertDoesNotExist()
        }
    }

    private fun addTask(taskName: String) {
        with(composeTestRule) {
            clickAddTask()
            onNode(hasSetTextAction()).performTextInput(taskName)
            onNodeWithText(string(TaskR.string.task_add_save)).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
        }
    }

    private fun clickAddTask() {
        composeTestRule.onNodeWithContentDescription(
            string(TaskR.string.task_cd_add_task),
            useUnmergedTree = true
        ).performClick()
    }

    private fun ComposeTestRule.onCheckbox(name: String) = onNode(
        SemanticsMatcher.expectValue(CheckboxNameKey, name)
    )

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)
}
