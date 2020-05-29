package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UpdateCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategoryUseCase = InsertCategory(categoryRepository)

    private val loadCategoryUseCase = LoadCategory(categoryRepository)

    private val updateCategoryUseCase = UpdateCategory(categoryRepository)

    @Before
    fun setup() = runBlockingTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is updated`() = runBlockingTest {
        val category = Category(id = 24, name = "toys", color = "#04206F")
        addCategoryUseCase(category)

        val updatedCategory = category.copy(name = "toyz", color = "#6969AA")
        updateCategoryUseCase(updatedCategory)

        val result = loadCategoryUseCase(category.id)
        Assert.assertEquals(updatedCategory, result)
    }
}
