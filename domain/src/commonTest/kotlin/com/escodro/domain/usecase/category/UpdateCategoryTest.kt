package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.implementation.AddCategoryImpl
import com.escodro.domain.usecase.category.implementation.LoadCategoryImpl
import com.escodro.domain.usecase.category.implementation.UpdateCategoryImpl
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class UpdateCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategoryUseCase = AddCategoryImpl(categoryRepository)

    private val loadCategoryUseCase = LoadCategoryImpl(categoryRepository)

    private val updateCategoryUseCase = UpdateCategoryImpl(categoryRepository)

    @BeforeTest
    fun setup() = runTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is updated`() = runTest {
        val category = Category(id = 24, name = "toys", color = "#04206F")
        addCategoryUseCase(category)

        val updatedCategory = category.copy(name = "toyz", color = "#6969AA")
        updateCategoryUseCase(updatedCategory)

        val result = loadCategoryUseCase(category.id)
        assertEquals(updatedCategory, result)
    }
}
