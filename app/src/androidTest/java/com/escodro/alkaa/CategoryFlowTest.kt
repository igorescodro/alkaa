package com.escodro.alkaa

import androidx.compose.ui.graphics.Color
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
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.util.WindowSizeClassFake
import com.escodro.androidtest.rule.DisableAnimationsRule
import com.escodro.category.presentation.semantics.ColorKey
import com.escodro.designsystem.AlkaaTheme
import com.escodro.local.dao.CategoryDao
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

internal class CategoryFlowTest : KoinTest {

    private val categoryDao: CategoryDao by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        runTest {
            // Clean all existing categories
            categoryDao.cleanTable()
        }

        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph(windowSizeClass = WindowSizeClassFake.Phone)
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
            onNodeWithText("Save").performClick()

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
            onNodeWithContentDescription("Remove").performClick()
            onNodeWithText("Remove").performClick()

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
            onNodeWithText("Save").performClick()

            // Validate updated color
            onCategoryColorItem(color).assertExists()
        }
    }

    private fun addCategory(name: String) {
        with(composeTestRule) {
            onNodeWithContentDescription("Add category", useUnmergedTree = true).performClick()
            onNode(hasSetTextAction()).performTextInput(name)
            onNodeWithText("Save").performClick()
            onNodeWithText(text = name, useUnmergedTree = true).assertExists()
        }
    }

    private fun navigateToCategory() {
        composeTestRule.onNodeWithContentDescription(label = "Categories", useUnmergedTree = true)
            .performClick()
    }

    private fun ComposeTestRule.onCategoryColorItem(color: Color) = onNode(
        SemanticsMatcher.expectValue(ColorKey, color),
    )
}
