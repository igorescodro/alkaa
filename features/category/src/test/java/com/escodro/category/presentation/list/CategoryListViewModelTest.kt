package com.escodro.category.presentation.list

import com.escodro.category.fake.FAKE_DOMAIN_CATEGORY_LIST
import com.escodro.category.fake.LoadAllCategoriesFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CategoryListViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val loadAllCategories = LoadAllCategoriesFake()

    private val categoryMapper = CategoryMapper()

    private val viewModel = CategoryListViewModelImpl(loadAllCategories, categoryMapper)

    @Test
    fun `test if when there is categories created than it returns the success state with them`() =
        runBlockingTest {

            // Given the viewModel is called to load the categories
            loadAllCategories.categoriesToBeReturned = FAKE_DOMAIN_CATEGORY_LIST
            val flow = viewModel.loadCategories()

            // When the latest event is collected
            val state = flow.first()

            // Then the category list is returned
            require(state is CategoryState.Loaded)
            val assertCategoryList = categoryMapper.toView(FAKE_DOMAIN_CATEGORY_LIST)
            Assert.assertEquals(assertCategoryList, state.categoryList)
        }

    @Test
    fun `test if when there is no categories created than it returns the success state with empty list`() =
        runBlockingTest {
            // Given the viewModel is called to load the categories
            loadAllCategories.categoriesToBeReturned = listOf()
            val flow = viewModel.loadCategories()

            // When the latest event is collected
            val state = flow.first()

            require(state is CategoryState.Empty)
        }
}
