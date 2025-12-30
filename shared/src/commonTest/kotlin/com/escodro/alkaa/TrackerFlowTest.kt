package com.escodro.alkaa

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.local.dao.TaskDao
import com.escodro.resources.Res
import com.escodro.resources.tracker_header_empty
import com.escodro.task.presentation.list.CheckboxNameKey
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
internal class TrackerFlowTest : AlkaaTest(), KoinTest {

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
    fun open_clean_task_tracker_and_ensure_empty_message_is_shown() = uiTest {
        navigateToTracker()

        val emptyMessage = runBlocking {
            getString(Res.string.tracker_header_empty)
        }
        onNodeWithText(text = emptyMessage).assertExists()
    }

    @Test
    fun complete_task_and_ensure_it_is_shown_in_tracker() = uiTest {
        val taskName = "Completed task for tracker"
        onNodeWithContentDescription("Add task").performClick()
        onNode(hasSetTextAction()).performTextInput(taskName)
        onNodeWithText("Add").performClick()

        // Complete task
        onNode(SemanticsMatcher.expectValue(CheckboxNameKey, taskName)).performClick()

        navigateToTracker()

        // Completed tasks might be in the list
        onNodeWithText(text = "1 completed task", useUnmergedTree = true).assertExists()
    }

    private fun ComposeUiTest.navigateToTracker() {
        onNodeWithContentDescription(label = "More", useUnmergedTree = true).performClick()
        onNodeWithText(text = "Task Tracker").performClick()
    }
}
