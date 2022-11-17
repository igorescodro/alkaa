package com.escodro.category.presentation.bottomsheet

import android.graphics.Color
import com.escodro.category.fake.DeleteCategoryFake
import com.escodro.category.fake.FAKE_DOMAIN_CATEGORY
import com.escodro.category.fake.LoadCategoryFake
import com.escodro.category.fake.UpdateCategoryFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CategoryEditViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val loadCategoryFake = LoadCategoryFake()

    private val updateCategory = UpdateCategoryFake()

    private val deleteCategory = DeleteCategoryFake()

    private val mapper = CategoryMapper()

    private val viewModel = CategoryEditViewModel(
        loadCategoryUseCase = loadCategoryFake,
        updateCategoryUseCase = updateCategory,
        deleteCategoryUseCase = deleteCategory,
        applicationScope = TestScope(coroutinesRule.testDispatcher),
        mapper = mapper
    )

    @Before
    fun setup() {
        updateCategory.clear()
        deleteCategory.clear()
    }

    @Test
    fun `test if category is loaded when the id is valid`() = runTest {
        // Given the task is correctly returned
        loadCategoryFake.categoryToBeReturned = FAKE_DOMAIN_CATEGORY
        val flow = viewModel.loadCategory(FAKE_DOMAIN_CATEGORY.id)

        // When the latest event is collected
        val state = flow.first()

        // Then the state contain the loaded category
        require(state is CategorySheetState.Loaded)
        val assertCategoryView = mapper.toView(FAKE_DOMAIN_CATEGORY)
        Assert.assertEquals(assertCategoryView, state.category)
    }

    @Test
    fun `test if category is empty when the id is not valid`() = runTest {
        // Given the task id is invalid
        loadCategoryFake.categoryToBeReturned = null
        val flow = viewModel.loadCategory(666L)

        val state = flow.first()

        Assert.assertTrue(state is CategorySheetState.Empty)
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
