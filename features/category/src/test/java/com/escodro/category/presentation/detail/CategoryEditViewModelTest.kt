package com.escodro.category.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.edit.CategoryEditUIState
import com.escodro.category.presentation.edit.CategoryEditViewModel
import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.UpdateCategory
import com.escodro.test.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

internal class CategoryEditViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockLoadCategory = mockk<LoadCategory>(relaxed = true)

    private val mockUpdateCategory = mockk<UpdateCategory>(relaxed = true)

    private val categoryMapper = CategoryMapper()

    private val viewModel =
        CategoryEditViewModel(mockLoadCategory, mockUpdateCategory, categoryMapper)

    @Test
    fun `check if category is loaded`() {
        val category = Category(id = 10, name = "Pelé", color = "")
        coEvery { mockLoadCategory.invoke(any()) } returns category

        viewModel.loadCategory(category.id)

        val resultCategory = categoryMapper.toView(category)
        assert(viewModel.uiState.value == CategoryEditUIState.Loaded(resultCategory))
    }

    @Test
    fun `check if error is returned when category is not found`() {
        coEvery { mockLoadCategory.invoke(any()) } returns null

        viewModel.loadCategory(0L)

        assert(viewModel.uiState.value == CategoryEditUIState.Error)
    }

    @Test
    fun `check if category is updated`() {
        val category = Category(id = 15, name = "buy oranges", color = "#56DF08")
        coEvery { mockLoadCategory.invoke(any()) } returns category
        viewModel.loadCategory(categoryId = category.id)

        viewModel.saveCategory(name = category.name!!, color = category.color!!)

        assert(viewModel.uiState.value == CategoryEditUIState.Saved)
        coVerify { mockUpdateCategory.invoke(category) }
    }
}
