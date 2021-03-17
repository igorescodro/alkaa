package com.escodro.category.presentation

import android.graphics.Color
import com.escodro.category.fake.UpdateCategoryFake
import com.escodro.category.mapper.CategoryMapperImpl
import com.escodro.categoryapi.model.Category
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class CategoryEditViewModelTest {

    private val updateCategory = UpdateCategoryFake()

    private val mapper = CategoryMapperImpl()

    private val viewModel = CategoryEditViewModel(updateCategory, mapper)

    @Before
    fun setup() {
        updateCategory.clear()
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
}
