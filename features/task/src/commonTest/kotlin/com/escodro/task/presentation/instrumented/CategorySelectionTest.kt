package com.escodro.task.presentation.instrumented

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.task_detail_category_empty_list
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.test.AlkaaTest
import com.escodro.test.annotation.IgnoreOnDesktop
import com.escodro.test.extension.onChip
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class CategorySelectionTest : AlkaaTest() {

    @Test
    fun test_chipClickedIsTheChipSelected() = runComposeUiTest {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green.hashCode())
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta.hashCode())
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray.hashCode())
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded and a category is clicked
        loadCategorySelection(categoryList, null)
        onNodeWithText(text = category1.name).performClick()

        // Then only the clicked category is checked
        onChip(category1.name).assertIsSelected()
        onChip(category2.name).assertIsNotSelected()
        onChip(category3.name).assertIsNotSelected()
    }

    @Test
    @IgnoreOnDesktop
    fun test_latestChipClickedIsTheChipSelected() = runComposeUiTest {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green.hashCode())
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta.hashCode())
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray.hashCode())
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded in several category are clicked
        loadCategorySelection(categoryList, null)
        onNodeWithText(text = category1.name).performClick()
        onNodeWithText(text = category2.name).performClick()
        onNodeWithText(text = category3.name).performClick()

        // Then only the last clicked category is checked
        onChip(category1.name).assertIsNotSelected()
        onChip(category2.name).assertIsNotSelected()
        onChip(category3.name).assertIsSelected()
    }

    @Test
    @IgnoreOnDesktop
    fun test_categoryPassedViaParamIsTheCurrentCategory() = runComposeUiTest {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green.hashCode())
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta.hashCode())
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray.hashCode())
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded with a category already selected
        loadCategorySelection(categoryList, category2.id)

        // Then only the given category is checked
        onChip(category1.name).assertIsNotSelected()
        onChip(category2.name).assertIsSelected()
        onChip(category3.name).assertIsNotSelected()
    }

    @Test
    @IgnoreOnDesktop
    fun test_categoryIsUncheckedWhenClickedTwice() = runComposeUiTest {
        // Given a list of category
        val category1 = Category(id = 1, name = "Movies", color = Color.Green.hashCode())
        val category2 = Category(id = 2, name = "Books", color = Color.Magenta.hashCode())
        val category3 = Category(id = 3, name = "Grocery", color = Color.LightGray.hashCode())
        val categoryList = listOf(category1, category2, category3)

        // When the view is loaded and a category is clicked twice
        loadCategorySelection(categoryList, null)
        onNodeWithText(text = category1.name).performClick()
        onNodeWithText(text = category1.name).performClick()

        // Then no chip are checked in the list
        onChip(category1.name).assertIsNotSelected()
        onChip(category2.name).assertIsNotSelected()
        onChip(category3.name).assertIsNotSelected()
    }

    @Test
    @IgnoreOnDesktop
    fun test_emptyCategoryListShowInfo() = runComposeUiTest {
        // When the view is loaded with a empty category list
        setContent {
            AlkaaThemePreview {
                CategorySelection(
                    state = CategoryState.Empty,
                    currentCategory = null,
                    onCategoryChange = { },
                )
            }
        }

        // Then text informing there are no categories is shown
        val emptyTextInfo = runBlocking { getString(Res.string.task_detail_category_empty_list) }
        onNodeWithText(emptyTextInfo).assertExists()
    }

    private fun ComposeUiTest.loadCategorySelection(
        categories: List<Category>,
        currentCategory: Long?,
    ) {
        setContent {
            AlkaaThemePreview {
                CategorySelection(
                    state = CategoryState.Loaded(categories.toImmutableList()),
                    currentCategory = currentCategory,
                    onCategoryChange = { },
                )
            }
        }
    }
}
