package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.category.presentation.semantics.ColorKey
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.provider.DaoProvider
import com.escodro.test.FlakyTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import com.escodro.category.R as CategoryR

internal class CategoryFlowTest : FlakyTest(), KoinTest {

    private val daoProvider: DaoProvider by inject()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        runBlocking {
            // Clean all existing categories
            daoProvider.getCategoryDao().cleanTable()
        }

        setContent {
            AlkaaTheme {
                NavGraph()
            }
        }
        navigateToCategory()
    }

    @Test
    fun test_addCategory() {
        // Create a simple category and validate if it is visible in the list
        addCategory("Movies")
    }

    @Test
    fun test_renameCategory() {
        // Create a simple category
        val oldName = "Reads"
        val newName = "My reads"
        addCategory(oldName)

        with(composeTestRule) {
            // Open it again and rename it
            onNodeWithText(oldName).performClick()
            onNode(hasSetTextAction()).performTextReplacement(newName)
            onNodeWithText(string(CategoryR.string.category_sheet_save)).performClick()

            // Validate new name
            onNodeWithText(text = newName, useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun test_removeCategory() {
        val name = "TV Shows"
        addCategory(name)

        with(composeTestRule) {
            // Open it again and removes it
            onNodeWithText(name).performClick()
            onNodeWithContentDescription(
                string(CategoryR.string.category_cd_remove_category)
            ).performClick()
            onNodeWithText(string(CategoryR.string.category_dialog_remove_confirm)).performClick()

            // Validate is no longer in the list
            onNodeWithText(text = name, useUnmergedTree = true).assertDoesNotExist()
        }
    }

    @Test
    fun test_updateCategoryColor() {
        val name = "Anime"
        val color = Color(0xFFFFCA28)
        addCategory(name)

        with(composeTestRule) {
            // Open it again and choose YELLOW
            onNodeWithText(name).performClick()
            onCategoryColorItem(color).performClick()
            onNodeWithText(string(CategoryR.string.category_sheet_save)).performClick()

            // Validate updated color
            onCategoryColorItem(color).assertExists()
        }
    }

    private fun addCategory(name: String) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                string(CategoryR.string.category_cd_add_category),
                useUnmergedTree = true
            ).performClick()
            onNode(hasSetTextAction()).performTextInput(name)
            onNodeWithText(string(CategoryR.string.category_sheet_save)).performClick()
            onNodeWithText(text = name, useUnmergedTree = true).assertExists()
        }
    }

    private fun navigateToCategory() {
        composeTestRule.onNodeWithContentDescription(
            label = string(R.string.home_title_categories),
            useUnmergedTree = true
        ).performClick()
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)

    private fun ComposeTestRule.onCategoryColorItem(color: Color) = onNode(
        SemanticsMatcher.expectValue(ColorKey, color)
    )
}
