package com.escodro.search

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.designsystem.AlkaaTheme
import com.escodro.search.model.TaskSearchItem
import com.escodro.search.presentation.SearchScaffold
import com.escodro.search.presentation.SearchViewState
import org.junit.Rule
import org.junit.Test

internal class SearchSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_emptyViewIsShown() {
        // Given an empty state
        val state = SearchViewState.Empty

        // When the view is loaded
        loadTaskList(state)

        // Assert that the empty view is loaded
        val header = context.getString(R.string.search_header_empty)
        val contentDescription = context.getString(R.string.search_cd_empty_list)
        composeTestRule.onNodeWithText(text = header).assertExists()
        composeTestRule.onNodeWithContentDescription(label = contentDescription)
    }

    @Test
    fun test_listViewIsShown() {
        // Given a success state
        val item1 = TaskSearchItem(1, false, "Buy cocoa", Color.Yellow, false)
        val item2 = TaskSearchItem(1, false, "Send gift", Color.Green, false)
        val queryList = listOf(item1, item2)
        val state = SearchViewState.Loaded(queryList)

        // When the view is loaded
        loadTaskList(state)

        // Assert that the item are shown on the list
        composeTestRule.onNodeWithText(text = item1.title, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(text = item2.title, useUnmergedTree = true).assertExists()
    }

    private fun loadTaskList(state: SearchViewState) {
        composeTestRule.setContent {
            AlkaaTheme {
                SearchScaffold(
                    viewState = state,
                    onItemClick = {},
                    query = "",
                    setQuery = {}
                )
            }
        }
    }
}
