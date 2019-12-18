package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadAllCategoriesTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val loadAllCategories = LoadAllCategories(mockCategoryRepo)

    private lateinit var testObserver: TestSubscriber<List<Category>>

    @Before
    fun setup() {
        testObserver = TestSubscriber.create()
    }

    @Test
    fun `check if repo function was called`() {
        val mockList = mockk<List<Category>>()
        every { mockCategoryRepo.findAllCategories() } returns Flowable.fromArray(mockList)

        loadAllCategories().subscribe(testObserver)
        testObserver.assertValue(mockList)

        verify { mockCategoryRepo.findAllCategories() }
    }
}
