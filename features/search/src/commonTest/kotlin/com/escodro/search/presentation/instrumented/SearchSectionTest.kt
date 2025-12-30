package com.escodro.search.presentation.instrumented

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.resources.Res
import com.escodro.resources.search_cd_empty_list
import com.escodro.resources.search_header_empty
import com.escodro.search.model.TaskSearchItem
import com.escodro.search.presentation.SearchScaffold
import com.escodro.search.presentation.SearchViewState
import com.escodro.test.AlkaaTest
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class SearchSectionTest : AlkaaTest() {

    @Test
    fun when_view_is_opened_then_empty_view_is_shown() = runComposeUiTest {
        // Given an empty state
        val state = SearchViewState.Empty

        // When the view is loaded
        loadTaskList(state)

        // Assert that the empty view is loaded
        val header = runBlocking { getString(Res.string.search_header_empty) }
        val contentDescription = runBlocking { getString(Res.string.search_cd_empty_list) }
        onNodeWithText(text = header).assertExists()
        onNodeWithContentDescription(label = contentDescription).assertExists()
    }

    @Test
    fun when_view_has_items_than_items_are_shown() = runComposeUiTest {
        // Given a success state
        val item1 = TaskSearchItem(
            id = 1,
            isCompleted = false,
            title = "Buy cocoa",
            categoryColor = Color.Yellow,
            isRepeating = false,
        )
        val item2 = TaskSearchItem(
            id = 1,
            isCompleted = false,
            title = "Send gift",
            categoryColor = Color.Green,
            isRepeating = false,
        )
        val queryList = persistentListOf(item1, item2)
        val state = SearchViewState.Loaded(queryList)

        // When the view is loaded
        loadTaskList(state)

        // Assert that the item are shown on the list
        onNodeWithText(text = item1.title, useUnmergedTree = true).assertExists()
        onNodeWithText(text = item2.title, useUnmergedTree = true).assertExists()
    }

    private fun ComposeUiTest.loadTaskList(state: SearchViewState) {
        setContent {
            SearchScaffold(
                viewState = state,
                onItemClick = {},
                query = "",
                setQuery = {},
            )
        }
    }
}
