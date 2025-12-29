package com.escodro.category.presentation.list

import com.escodro.category.fake.FAKE_DOMAIN_CATEGORY_LIST
import com.escodro.category.fake.LoadAllCategoriesFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CategoryListViewModelTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadAllCategories = LoadAllCategoriesFake()

    private val categoryMapper = CategoryMapper()

    private val viewModel = CategoryListViewModelImpl(loadAllCategories, categoryMapper)

    @Test
    fun `test if when there is categories created than it returns the success state with them`() =
        runTest {
            // Given the viewModel is called to load the categories
            loadAllCategories.categoriesToBeReturned = FAKE_DOMAIN_CATEGORY_LIST
            val flow = viewModel.loadCategories()

            // When the latest event is collected
            val state = flow.first()

            // Then the category list is returned
            require(state is CategoryState.Loaded)
            val assertCategoryList = categoryMapper.toView(FAKE_DOMAIN_CATEGORY_LIST)
            assertEquals(expected = assertCategoryList, actual = state.categoryList)
        }

    @Test
    fun `test if when there is no categories created than it returns the success state with empty list`() =
        runTest {
            // Given the viewModel is called to load the categories
            loadAllCategories.categoriesToBeReturned = emptyList()
            val flow = viewModel.loadCategories()

            // When the latest event is collected
            val state = flow.first()

            require(state is CategoryState.Empty)
        }
}
