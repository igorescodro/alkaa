package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteCategoryTest {

    private val categoryRepository = CategoryRepositoryFake()

    private val addCategory = InsertCategory(categoryRepository)

    private val deleteCategory = DeleteCategory(categoryRepository)

    private val loadCategory = LoadCategory(categoryRepository)

    @Before
    fun setup() = runBlockingTest {
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if category is deleted`() = runBlockingTest {
        val category = Category(id = 13, name = "books to read", color = "#FFAA00")
        addCategory(category)
        deleteCategory(category)

        val result = loadCategory(category.id)
        Assert.assertNull(result)
    }
}
