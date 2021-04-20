package com.escodro.category.presentation.bottomsheet

import android.graphics.Color
import com.escodro.category.fake.DeleteCategoryFake
import com.escodro.category.fake.UpdateCategoryFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class CategoryEditViewModelTest {

    private val updateCategory = UpdateCategoryFake()

    private val deleteCategory = DeleteCategoryFake()

    private val mapper = CategoryMapper()

    private val viewModel = CategoryEditViewModel(updateCategory, deleteCategory, mapper)

    @Before
    fun setup() {
        updateCategory.clear()
        deleteCategory.clear()
    }

    @Test
    fun `test if category is updated`() {
        // Given the category to be updated
        val id = 234L
        val category = Category(id = id, name = "Cooking", color = Color.YELLOW)

        // When the add update is called
        viewModel.updateCategory(category)

        // Then the category is updated
        Assert.assertTrue(updateCategory.wasCategoryUpdated(id))
    }

    @Test
    fun `test if category without name is not updated`() {
        // Given the category to be updated without a name
        val id = 234L
        val category = Category(id = id, name = "", color = Color.YELLOW)

        // When the add update is called
        viewModel.updateCategory(category)

        // Then the category is not updated
        Assert.assertFalse(updateCategory.wasCategoryUpdated(id))
    }

    @Test
    fun `test if category is deleted`() {
        // Given the category to be removed
        val id = 234L
        val category = Category(id = id, name = "", color = Color.YELLOW)

        // When the remove function is called
        viewModel.deleteCategory(category)

        // Then the category is not updated
        Assert.assertTrue(deleteCategory.wasCategoryRemoved(id))
    }
}
