package com.escodro.task

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.category.ChipNameKey
import org.junit.Rule
import org.junit.Test

internal class CategorySelectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_chipClickedIsTheChipSelected() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = android.graphics.Color.GREEN)
        val category2 = Category(id = 2, name = "Books", color = android.graphics.Color.MAGENTA)
        val category3 = Category(id = 3, name = "Grocery", color = android.graphics.Color.LTGRAY)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded in a category is clicked
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name).performClick()

        // Then only the clicked category is checked
        composeTestRule.onChip(category1.name).assertIsChecked()
        composeTestRule.onChip(category2.name).assertIsUnchecked()
        composeTestRule.onChip(category3.name).assertIsUnchecked()
    }

    @Test
    fun test_latestChipClickedIsTheChipSelected() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = android.graphics.Color.GREEN)
        val category2 = Category(id = 2, name = "Books", color = android.graphics.Color.MAGENTA)
        val category3 = Category(id = 3, name = "Grocery", color = android.graphics.Color.LTGRAY)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded in several category are clicked
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name).performClick()
        composeTestRule.onNodeWithText(text = category2.name).performClick()
        composeTestRule.onNodeWithText(text = category3.name).performClick()

        // Then only the last clicked category is checked
        composeTestRule.onChip(category1.name).assertIsUnchecked()
        composeTestRule.onChip(category2.name).assertIsUnchecked()
        composeTestRule.onChip(category3.name).assertIsChecked()
    }

    @Test
    fun test_categoryPassedViaParamIsTheCurrentCategory() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = android.graphics.Color.GREEN)
        val category2 = Category(id = 2, name = "Books", color = android.graphics.Color.MAGENTA)
        val category3 = Category(id = 3, name = "Grocery", color = android.graphics.Color.LTGRAY)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded with a category already selected
        loadCategorySelection(categoryList, category2.id)

        // Then only the given category is checked
        composeTestRule.onChip(category1.name).assertIsUnchecked()
        composeTestRule.onChip(category2.name).assertIsChecked()
        composeTestRule.onChip(category3.name).assertIsUnchecked()
    }

    @Test
    fun test_categoryIsUncheckedWhenClickedTwice() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = android.graphics.Color.GREEN)
        val category2 = Category(id = 2, name = "Books", color = android.graphics.Color.MAGENTA)
        val category3 = Category(id = 3, name = "Grocery", color = android.graphics.Color.LTGRAY)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded and a category is clicked twice
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name).performClick()
        composeTestRule.onNodeWithText(text = category1.name).performClick()

        // Then no chip are checked in the list
        composeTestRule.onChip(category1.name).assertIsUnchecked()
        composeTestRule.onChip(category2.name).assertIsUnchecked()
        composeTestRule.onChip(category3.name).assertIsUnchecked()
    }

    @Test
    fun test_emptyCategoryListShowInfo() {
        // When the view is loaded with a empty category list
        composeTestRule.setContent {
            AlkaaTheme {
                CategorySelection(
                    state = CategoryState.Empty,
                    currentCategory = null,
                    onCategoryChange = { }
                )
            }
        }

        // Then text informing there are no categories is shown
        val emptyTextInfo = context.getString(R.string.task_detail_category_empty_list)
        composeTestRule.onNodeWithText(emptyTextInfo).assertExists()
    }

    private fun loadCategorySelection(categories: List<Category>, currentCategory: Long?) {
        composeTestRule.setContent {
            AlkaaTheme {
                CategorySelection(
                    state = CategoryState.Loaded(categories),
                    currentCategory = currentCategory,
                    onCategoryChange = { }
                )
            }
        }
    }

    private fun SemanticsNodeInteraction.assertIsChecked() = assert(
        SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.On)
    )

    private fun SemanticsNodeInteraction.assertIsUnchecked() = assert(
        SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.Off)
    )

    private fun ComposeTestRule.onChip(chipName: String) = onNode(
        SemanticsMatcher.expectValue(ChipNameKey, chipName)
    )
}
