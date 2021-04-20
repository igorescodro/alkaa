package com.escodro.category.presentation.bottomsheet

import android.graphics.Color
import com.escodro.category.fake.AddCategoryFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.core.extension.toStringColor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class CategoryAddViewModelTest {

    private val addCategory = AddCategoryFake()

    private val categoryMapper = CategoryMapper()

    private val viewModel = CategoryAddViewModel(addCategory, categoryMapper)

    @Before
    fun setup() {
        addCategory.clear()
    }

    @Test
    fun `test if category is added`() {
        // Given the category to be added
        val name = "Beer"
        val color = Color.parseColor("#9CCC65")
        val category = Category(name = name, color = color)

        // When the add function is called
        viewModel.addCategory(category = category)

        // Then the category is added
        Assert.assertTrue(addCategory.wasCategoryCreated(name))
        Assert.assertEquals(color.toStringColor(), addCategory.getCategory(name)!!.color)
    }

    @Test
    fun `test if category without name is not added`() {
        // Given the category without name
        val name = ""
        val color = Color.parseColor("#9CCC65")
        val category = Category(name = name, color = color)

        // When the add function is called
        viewModel.addCategory(category = category)

        // Then the category is not added
        Assert.assertFalse(addCategory.wasCategoryCreated(name))
    }
}
