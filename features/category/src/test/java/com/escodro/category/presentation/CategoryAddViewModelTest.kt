package com.escodro.category.presentation

import android.graphics.Color
import com.escodro.category.fake.AddCategoryFake
import com.escodro.category.mapper.CategoryMapperImpl
import com.escodro.core.extension.toStringColor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class CategoryAddViewModelTest {

    private val addCategory = AddCategoryFake()

    private val categoryMapper = CategoryMapperImpl()

    private val viewModel = CategoryAddViewModel(addCategory, categoryMapper)

    @Before
    fun setup() {
        addCategory.clear()
    }

    @Test
    fun `test if category is added`() {
        val name = "Beer"
        val color = Color.parseColor("#9CCC65")

        viewModel.addCategory(name = name, color = color)

        Assert.assertTrue(addCategory.wasCategoryCreated(name))
        Assert.assertEquals(color.toStringColor(), addCategory.getCategory(name)!!.color)
    }

    @Test
    fun `test if category without name is not added`() {
        val name = ""
        val color = Color.parseColor("#9CCC65")

        viewModel.addCategory(name = name, color = color)

        Assert.assertFalse(addCategory.wasCategoryCreated(name))
    }
}
