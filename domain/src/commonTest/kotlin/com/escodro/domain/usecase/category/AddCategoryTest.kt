package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.implementation.AddCategoryImpl
import com.escodro.domain.usecase.category.implementation.LoadAllCategoriesImpl
import com.escodro.domain.usecase.category.implementation.LoadCategoryImpl
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AddCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategoryUseCase = AddCategoryImpl(categoryRepository)

    private val loadCategoryUseCase = LoadCategoryImpl(categoryRepository)

    private val loadAllCategoriesUseCase = LoadAllCategoriesImpl(categoryRepository)

    @BeforeTest
    fun setup() = runTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is correctly added`() = runTest {
        val category = Category(id = 22, name = "shopping list", color = "#122100")
        addCategoryUseCase(category)

        val result = loadCategoryUseCase(category.id)
        assertEquals(category, result)
    }

    @Test
    fun `test if category with empty title is not added`() = runTest {
        val category = Category(id = 44, name = "   ", color = "#876782")
        addCategoryUseCase(category)

        val result = loadCategoryUseCase(category.id)
        assertNull(result)
    }

    @Test
    fun `test if all category are added`() = runTest {
        val assertList = mutableListOf<Category>()
        for (iterator in 1..100) {
            val category = Category(id = iterator.toLong(), name = "$iterator", color = "#5567FA")
            addCategoryUseCase(category)
            assertList.add(category)
        }

        val resultList = loadAllCategoriesUseCase().first()

        assertEquals(assertList, resultList)
    }
}
