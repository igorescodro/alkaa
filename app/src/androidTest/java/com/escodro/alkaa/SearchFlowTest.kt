package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.fake.FAKE_TASKS
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.util.WindowSizeClassFake
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.provider.DaoProvider
import com.escodro.test.extension.waitUntilNotExists
import com.escodro.test.rule.DisableAnimationsRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import com.escodro.search.R as SearchR

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchFlowTest : KoinTest {

    private val daoProvider: DaoProvider by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        runTest {
            // Clean all existing tasks
            daoProvider.getTaskDao().cleanTable()

            // Add some fake tasks
            FAKE_TASKS.forEach { task -> daoProvider.getTaskDao().insertTask(task) }
        }

        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph(windowSizeClass = WindowSizeClassFake.Phone)
            }
        }

        navigateToSearch()
    }

    @Test
    fun test_allTasksAreShownWithoutQuery() {
        // Without typing anything on query text field

        with(composeTestRule) {
            FAKE_TASKS.forEach { task ->
                // Validate all tasks are shown
                onNodeWithText(text = task.title, useUnmergedTree = true).assertExists()
            }
        }
    }

    @Test
    fun test_onlyMatchingQueryIsShown() {
        with(composeTestRule) {
            // Type the first task as query and validate it is shown in the list
            val query = FAKE_TASKS.first().title
            onNode(hasSetTextAction()).performTextInput(query)
            onAllNodesWithText(text = query, useUnmergedTree = true)[1].assertExists()

            // Wait until the other second item is no longer visible
            waitUntilNotExists(hasText(FAKE_TASKS[1].title))

            // Drop the first task and validate others are not shown
            FAKE_TASKS.drop(1).forEach { task ->
                // Validate all tasks are shown
                onNodeWithText(text = task.title, useUnmergedTree = true).assertDoesNotExist()
            }
        }
    }

    @Test
    fun test_noTasksAreShownWithInvalidQuery() {
        with(composeTestRule) {
            onNode(hasSetTextAction()).performTextInput("query")

            // Wait until the first task is not visible
            waitUntilNotExists(hasText(FAKE_TASKS[0].title))

            FAKE_TASKS.forEach { task ->
                // Validate all tasks are shown
                onNodeWithText(text = task.title, useUnmergedTree = true).assertDoesNotExist()
            }

            onNodeWithContentDescription(string(SearchR.string.search_cd_empty_list)).assertExists()
            onNodeWithText(text = string(SearchR.string.search_header_empty)).assertExists()
        }
    }

    private fun navigateToSearch() {
        composeTestRule.onNodeWithContentDescription(
            label = string(R.string.home_title_search),
            useUnmergedTree = true,
        ).performClick()
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)
}
