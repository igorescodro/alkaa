package com.escodro.category.presentation.bottomsheet

import com.escodro.category.fake.AddCategoryFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.designsystem.extensions.toArgbColor
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CategoryAddViewModelTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val addCategory = AddCategoryFake()

    private val categoryMapper = CategoryMapper()

    private val viewModel = CategoryAddViewModel(
        addCategoryUseCase = addCategory,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
        categoryMapper,
    )

    @BeforeTest
    fun setup() {
        addCategory.clear()
    }

    @Test
    fun `test if category is added`() {
        // Given the category to be added
        val name = "Beer"
        val color = "#9CCC65".toArgbColor()
        val category = Category(name = name, color = color)

        // When the add function is called
        viewModel.addCategory(category = category)

        // Then the category is added
        assertTrue(this.addCategory.wasCategoryCreated(name))
        assertEquals(expected = color, actual = addCategory.getCategory(name)!!.color.toArgbColor())
    }

    @Test
    fun `test if category without name is not added`() {
        // Given the category without name
        val name = ""
        val color = 10_101_010
        val category = Category(name = name, color = color)

        // When the add function is called
        viewModel.addCategory(category = category)

        // Then the category is not added
        assertFalse(this.addCategory.wasCategoryCreated(name))
    }
}
