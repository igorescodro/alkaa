package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

internal class UpdateCategoryTest {

    private val mockTask = mockk<Category>(relaxed = true)

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val updateCategory = UpdateCategory(mockCategoryRepo)

    @Test
    fun `check if repo function was called`() = runBlockingTest {
        updateCategory(mockTask)
        coVerify { mockCategoryRepo.updateCategory(mockTask) }
    }
}
