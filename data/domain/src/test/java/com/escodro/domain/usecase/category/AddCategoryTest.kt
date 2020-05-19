package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class AddCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategoryUseCase = InsertCategory(categoryRepository)

    private val loadCategoryUseCase = LoadCategory(categoryRepository)

    private val loadAllCategoriesUseCase = LoadAllCategories(categoryRepository)

    @Before
    fun setup() = runBlockingTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is correctly added`() = runBlockingTest {
        val category = Category(id = 22, name = "shopping list", color = "#122100")
        addCategoryUseCase(category)

        val result = loadCategoryUseCase(category.id)
        Assert.assertEquals(category, result)
    }

    @Test
    fun `test if category with empty title is not added`() = runBlockingTest {
        val category = Category(id = 44, name = "   ", color = "#876782")
        addCategoryUseCase(category)

        val result = loadCategoryUseCase(category.id)
        Assert.assertNull(result)
    }

    @Test
    fun `test if all category are added`() = runBlockingTest {
        val assertList = mutableListOf<Category>()
        for (iterator in 1..100) {
            val category = Category(id = iterator.toLong(), name = "$iterator", color = "#5567FA")
            addCategoryUseCase(category)
            assertList.add(category)
        }

        val resultList = loadAllCategoriesUseCase().first()

        Assert.assertArrayEquals(assertList.toTypedArray(), resultList.toTypedArray())
    }
}
