@file:OptIn(ExperimentalTestApi::class)

package com.escodro.alkaa

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
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
import com.escodro.designsystem.config.DesignSystemConfig
import com.escodro.designsystem.semantics.ColorKey
import com.escodro.local.dao.CategoryDao
import com.escodro.resources.Res
import com.escodro.resources.kuvio_add_button_cd
import com.escodro.resources.kuvio_add_task_bar_placeholder
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CategoryFlowTest : AlkaaTest(), KoinTest {

    private val categoryDao: CategoryDao by inject()

    @BeforeTest
    fun setup() {
        DesignSystemConfig.isNewDesignEnabled = false
        beforeTest()
        runTest {
            // Clean all existing categories
            categoryDao.cleanTable()
        }
    }

    @AfterTest
    fun tearDown() {
        DesignSystemConfig.isNewDesignEnabled = false
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

    @Test
    fun when_category_is_clicked_and_flag_enabled_then_details_screen_is_shown() = uiTest {
        // Given
        DesignSystemConfig.isNewDesignEnabled = true
        navigateToCategory()
        addCategory("Fitness")

        // When
        onNodeWithText("Fitness").performClick()

        // Then — details screen header with category name is displayed
        onNodeWithText("Fitness").assertIsDisplayed()
        // Bottom sheet "Save" button is absent
        onNodeWithText("Save").assertDoesNotExist()
    }

    @Test
    fun when_category_is_clicked_and_flag_disabled_then_bottom_sheet_is_shown() = uiTest {
        // Given — flag is false (reset in @BeforeTest)
        navigateToCategory()
        addCategory("Hobbies")

        // When
        onNodeWithText("Hobbies").performClick()

        // Then — bottom sheet is present (Save button visible)
        onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun when_task_is_added_in_category_details_then_it_appears_in_the_list() = uiTest {
        // Given
        DesignSystemConfig.isNewDesignEnabled = true
        navigateToCategory()
        addCategory("Finance")
        onNodeWithText("Finance").performClick()

        // When — type in the AddTaskBar and submit
        val addBarPlaceholder = runBlocking { getString(Res.string.kuvio_add_task_bar_placeholder) }
        onNodeWithText(addBarPlaceholder).performClick()
        onNodeWithText(addBarPlaceholder).performTextInput("Buy groceries")
        onNodeWithContentDescription(
            runBlocking { getString(Res.string.kuvio_add_button_cd) },
        ).performClick()

        // Then
        onNodeWithText("Buy groceries").assertIsDisplayed()
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
