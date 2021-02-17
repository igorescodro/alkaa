package com.escodro.task

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.task.model.Category
import com.escodro.task.presentation.detail.CategorySelection
import com.escodro.task.presentation.detail.ChipNameKey
import com.escodro.theme.AlkaaTheme
import org.junit.Rule
import org.junit.Test

internal class CategorySelectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assert_ChipClickedIsTheChipSelected() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green)
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta)
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded in a category is clicked
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name!!).performClick()

        // Then only the clicked category is checked
        composeTestRule.onChip(category1.name!!).assertIsChecked()
        composeTestRule.onChip(category2.name!!).assertIsUnchecked()
        composeTestRule.onChip(category3.name!!).assertIsUnchecked()
    }

    @Test
    fun assert_LatestChipClickedIsTheChipSelected() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green)
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta)
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded in several category are clicked
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name!!).performClick()
        composeTestRule.onNodeWithText(text = category2.name!!).performClick()
        composeTestRule.onNodeWithText(text = category3.name!!).performClick()

        // Then only the last clicked category is checked
        composeTestRule.onChip(category1.name!!).assertIsUnchecked()
        composeTestRule.onChip(category2.name!!).assertIsUnchecked()
        composeTestRule.onChip(category3.name!!).assertIsChecked()
    }

    @Test
    fun assert_CategoryPassedViaParamIsTheCurrentCategory() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green)
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta)
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded with a category already selected
        loadCategorySelection(categoryList, category2.id)

        // Then only the given category is checked
        composeTestRule.onChip(category1.name!!).assertIsUnchecked()
        composeTestRule.onChip(category2.name!!).assertIsChecked()
        composeTestRule.onChip(category3.name!!).assertIsUnchecked()
    }

    @Test
    fun assert_CategoryIsUncheckedWhenClickedTwice() {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green)
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta)
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray)
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded and a category is clicked twice
        loadCategorySelection(categoryList, null)
        composeTestRule.onNodeWithText(text = category1.name!!).performClick()
        composeTestRule.onNodeWithText(text = category1.name!!).performClick()

        // Then no chip are checked in the list
        composeTestRule.onChip(category1.name!!).assertIsUnchecked()
        composeTestRule.onChip(category2.name!!).assertIsUnchecked()
        composeTestRule.onChip(category3.name!!).assertIsUnchecked()
    }

    private fun loadCategorySelection(categories: List<Category>, currentCategory: Long?) {
        composeTestRule.setContent {
            AlkaaTheme {
                CategorySelection(
                    categories = categories,
                    currentCategory = currentCategory,
                    onCategoryChanged = { }
                )
            }
        }
    }

    private fun SemanticsNodeInteraction.assertIsChecked() =
        assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Checked"))

    private fun SemanticsNodeInteraction.assertIsUnchecked() =
        assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Unchecked"))

    private fun ComposeTestRule.onChip(chipName: String) = onNode(
        SemanticsMatcher.expectValue(ChipNameKey, chipName)
    )
}
