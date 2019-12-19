package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class LoadCategoryTest {

    private val mockCategory = mockk<Category>(relaxed = true)

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val loadCategory = LoadCategory(mockCategoryRepo)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        coEvery { mockCategoryRepo.findCategoryById(mockCategory.id) } returns mockCategory

        loadCategory(mockCategory.id)

        coVerify { mockCategoryRepo.findCategoryById(mockCategory.id) }
    }
}
