@file:OptIn(ExperimentalTestApi::class)

package com.escodro.alkaa

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.waitUntilDoesNotExist
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.designsystem.semantics.ColorKey
import com.escodro.local.dao.CategoryDao
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CategoryFlowTest : AlkaaTest(), KoinTest {

    private val categoryDao: CategoryDao by inject()

    @BeforeTest
    fun setup() {
        beforeTest()
        runTest {
            // Clean all existing categories
            categoryDao.cleanTable()
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun add_category() = uiTest {
        navigateToCategory()
        // Create a simple category and validate if it is visible in the list
        addCategory("Movies")
    }

    @Test
    fun rename_category() = uiTest {
        navigateToCategory()

        // Create a simple category
        val oldName = "Reads"
        val newName = "My reads"
        addCategory(oldName)

        // Open it again and rename it
        onNodeWithText(oldName).performClick()
        onNode(hasSetTextAction()).performTextReplacement(newName)
        onNodeWithText("Save").performClick()

        // Validate new name
        onNodeWithText(text = newName, useUnmergedTree = true).assertExists()
    }

    @Test
    fun remove_category() = uiTest {
        navigateToCategory()

        val name = "TV Shows"
        addCategory(name)

        // Open it again and removes it
        onNodeWithText(name).performClick()
        onNodeWithContentDescription("Remove").performClick()
        onNodeWithText("Remove").performClick()

        // Validate is no longer in the list
        waitUntilDoesNotExist(hasText(text = name))
    }

    @Test
    fun update_category_color() = uiTest {
        navigateToCategory()

        val name = "Anime"
        val color = Color(0xFFFFCA28)
        addCategory(name)

        // Open it again and choose YELLOW
        onNodeWithText(name).performClick()
        onCategoryColorItem(color).performClick()
        onNodeWithText("Save").performClick()

        // Validate updated color
        onCategoryColorItem(color).assertExists()
    }

    @Test
    fun add_category_with_custom_color() = uiTest {
        navigateToCategory()

        val name = "Health"
        val color = Color(0xFF9CCC65) // GREEN from CategoryColors

        onNodeWithContentDescription("Add category", useUnmergedTree = true).performClick()
        onNode(hasSetTextAction()).performTextInput(name)
        onCategoryColorItem(color).performClick()
        onNodeWithText("Save").performClick()

        // Validate if it is visible in the list and has the correct color
        onNodeWithText(text = name, useUnmergedTree = true).assertExists()
        onCategoryColorItem(color).assertExists()
    }

    private fun ComposeUiTest.addCategory(name: String) {
        onNodeWithContentDescription("Add category", useUnmergedTree = true).performClick()
        onNode(hasSetTextAction()).performTextInput(name)
        onNodeWithText("Save").performClick()
        onAllNodesWithText(text = name, useUnmergedTree = true).onLast().assertExists()
    }

    private fun ComposeUiTest.navigateToCategory() {
        onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()
    }

    private fun ComposeUiTest.onCategoryColorItem(color: Color) = onNode(
        SemanticsMatcher.expectValue(ColorKey, color),
    )
}
