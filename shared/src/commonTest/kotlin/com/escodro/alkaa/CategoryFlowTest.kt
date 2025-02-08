@file:OptIn(ExperimentalTestApi::class)

package com.escodro.alkaa

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.waitUntilDoesNotExist
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.category.presentation.semantics.ColorKey
import com.escodro.local.dao.CategoryDao
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CategoryFlowTest : KoinTest {

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

    private fun ComposeUiTest.addCategory(name: String) {
        onNodeWithContentDescription("Add category", useUnmergedTree = true).performClick()
        onNode(hasSetTextAction()).performTextInput(name)
        onNodeWithText("Save").performClick()
        onNodeWithText(text = name, useUnmergedTree = true).assertExists()
    }

    private fun ComposeUiTest.navigateToCategory() {
        onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()
    }

    private fun ComposeUiTest.onCategoryColorItem(color: Color) = onNode(
        SemanticsMatcher.expectValue(ColorKey, color),
    )
}
