package com.escodro.category.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.add.CategoryAddUIState
import com.escodro.category.presentation.add.CategoryAddViewModel
import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.InsertCategory
import com.escodro.test.CoroutineTestRule
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

internal class CategoryAddViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockInsertCategory = mockk<InsertCategory>(relaxed = true)

    private val categoryMapper = CategoryMapper()

    private val viewModel = CategoryAddViewModel(mockInsertCategory, categoryMapper)

    @Test
    fun `check if new category is saved`() {
        val category = Category(name = "buy pineapples", color = "#34FD00")
        viewModel.saveCategory(name = category.name!!, color = category.color!!)

        assert(viewModel.uiState.value == CategoryAddUIState.Saved)
        coVerify { mockInsertCategory.invoke(category) }
    }

    @Test
    fun `check if empty category name shows EmptyName state`() {
        viewModel.saveCategory(name = "", color = "")

        assert(viewModel.uiState.value == CategoryAddUIState.EmptyName)
    }
}
