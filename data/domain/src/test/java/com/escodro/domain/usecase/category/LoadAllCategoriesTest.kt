package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class LoadAllCategoriesTest {

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val loadAllCategories = LoadAllCategories(mockCategoryRepo)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        val mockList = mockk<List<Category>>()
        coEvery { mockCategoryRepo.findAllCategories() } returns flow { emit(mockList) }

        val categories = loadAllCategories().first()
        Assert.assertEquals(mockList, categories)

        coVerify { mockCategoryRepo.findAllCategories() }
    }
}
