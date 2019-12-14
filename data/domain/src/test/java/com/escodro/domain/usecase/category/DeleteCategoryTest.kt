package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteCategoryTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Category>(relaxed = true)

    private val mockCategoryRepo = mockk<CategoryRepository>(relaxed = true)

    private val deleteCategory = DeleteCategory(mockCategoryRepo)

    private lateinit var testObserver: TestObserver<Completable>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun `check if repo function was called`() {
        every { mockCategoryRepo.deleteCategory(mockTask) } returns Completable.complete()

        deleteCategory(mockTask).subscribe(testObserver)
        testObserver.assertComplete()

        verify { mockCategoryRepo.deleteCategory(mockTask) }
    }
}
