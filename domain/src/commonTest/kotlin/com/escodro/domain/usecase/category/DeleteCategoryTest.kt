package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.implementation.AddCategoryImpl
import com.escodro.domain.usecase.category.implementation.DeleteCategoryImpl
import com.escodro.domain.usecase.category.implementation.LoadCategoryImpl
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

class DeleteCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategory = AddCategoryImpl(categoryRepository)

    private val deleteCategory = DeleteCategoryImpl(categoryRepository)

    private val loadCategory = LoadCategoryImpl(categoryRepository)

    @BeforeTest
    fun setup() = runTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is deleted`() = runTest {
        val category = Category(id = 13, name = "books to read", color = "#FFAA00")
        addCategory(category)
        deleteCategory(category)

        val result = loadCategory(category.id)
        assertNull(result)
    }
}
