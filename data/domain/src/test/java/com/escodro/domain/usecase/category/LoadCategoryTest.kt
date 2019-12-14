package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadCategoryTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockCategory = mockk<Category>(relaxed = true)

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val loadAllCategories = LoadCategory(mockCategoryRepo)

    private lateinit var testObserver: TestObserver<Category>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun `check if repo function was called`() {
        every { mockCategoryRepo.findCategoryById(mockCategory.id) } returns
            Single.just(mockCategory)

        loadAllCategories(mockCategory.id).subscribe(testObserver)

        verify { mockCategoryRepo.findCategoryById(mockCategory.id) }
    }
}
