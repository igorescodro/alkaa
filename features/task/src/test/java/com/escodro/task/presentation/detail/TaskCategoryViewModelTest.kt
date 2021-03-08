package com.escodro.task.presentation.detail

import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.presentation.category.CategoryState
import com.escodro.task.presentation.detail.category.TaskCategoryViewModel
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.main.TaskId
import com.escodro.task.presentation.fake.FAKE_DOMAIN_CATEGORY_LIST
import com.escodro.task.presentation.fake.LoadAllCategoriesFake
import com.escodro.task.presentation.fake.UpdateTaskCategoryFake
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskCategoryViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val loadAllCategories = LoadAllCategoriesFake()

    private val updateTaskCategory = UpdateTaskCategoryFake()

    private val categoryMapper = CategoryMapper()

    private val viewModel =
        TaskCategoryViewModel(loadAllCategories, updateTaskCategory, categoryMapper)

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

    @Test
    fun `test if when update category is called, the category is updated`() = runBlockingTest {
        // Given the viewModel is called to load the categories
        loadAllCategories.categoriesToBeReturned = FAKE_DOMAIN_CATEGORY_LIST
        viewModel.loadCategories()

        // When the category id is updated
        val taskId = 15L
        val newCategoryId = 4L
        viewModel.updateCategory(taskId = TaskId(taskId), categoryId = CategoryId(newCategoryId))

        // Then the task will be updated with given category id
        Assert.assertTrue(updateTaskCategory.isCategoryUpdated(taskId))
        val updatedCategoryId = updateTaskCategory.getUpdatedCategory(taskId)
        Assert.assertEquals(newCategoryId, updatedCategoryId)
    }

    @Test
    fun `test if when update category is called with null, the category is updated`() =
        runBlockingTest {

            // Given the viewModel is called to load the categories
            loadAllCategories.categoriesToBeReturned = FAKE_DOMAIN_CATEGORY_LIST
            viewModel.loadCategories()

            // When the category id is updated
            val taskId = 15L
            viewModel.updateCategory(taskId = TaskId(taskId), categoryId = CategoryId(null))

            // Then the task will be updated with given category id
            Assert.assertTrue(updateTaskCategory.isCategoryUpdated(taskId))
            val updatedCategoryId = updateTaskCategory.getUpdatedCategory(taskId)
            Assert.assertNull(updatedCategoryId)
        }
}
