package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.fake.CoroutinesDebouncerFake
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.provider.DaoProvider
import com.escodro.task.presentation.category.ChipNameKey
import com.escodro.test.assertIsChecked
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declare

internal class TaskFlowTest : KoinTest {

    private val daoProvider: DaoProvider by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        // Clean all existing tasks
        runBlocking { daoProvider.getTaskDao().cleanTable() }

        // Replace Debouncer with a Immediate Executor
        declare<CoroutineDebouncer> { CoroutinesDebouncerFake() }

        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph()
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
            val category = string(R.string.category_default_shopping)
            onChip(category).performClick()
            pressBack()

            // Reopen the task and validate if the category is selected
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            composeTestRule.onChip(category).assertIsChecked()
        }
    }

    private fun addAndOpenTask(taskName: String) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                string(R.string.task_cd_add_task),
                useUnmergedTree = true
            ).performClick()
            onNode(hasSetTextAction()).performTextInput(taskName)
            onNodeWithText(string(R.string.task_add_save)).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).performClick()
            onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
        }
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)

    private fun pressBack() {
        composeTestRule.onNodeWithContentDescription(
            string(R.string.back_arrow_cd),
            useUnmergedTree = true
        ).performClick()
    }

    private fun ComposeTestRule.onChip(chipName: String) = onNode(
        SemanticsMatcher.expectValue(ChipNameKey, chipName)
    )
}
