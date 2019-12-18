package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadCompletedTasksTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockRepo = mockk<TaskWithCategoryRepository>(relaxed = true)

    private val loadCompletedTasks = LoadCompletedTasks(mockRepo)

    private lateinit var testObserver: TestSubscriber<List<TaskWithCategory>>

    @Before
    fun setup() {
        testObserver = TestSubscriber.create()
    }

    @Test
    fun `check if repo tasks are filtered by completed`() {
        val category1 = Category(1, "Category A", "#FFFFFF")

        val task1 = Task(1, false, "Task 1")
        val task2 = Task(2, true, "Task 2")
        val task3 = Task(3, true, "Task 3")
        val task4 = Task(4, false, "Task 4")

        val mockList = listOf(
            TaskWithCategory(task1, category1),
            TaskWithCategory(task2, null),
            TaskWithCategory(task3, null),
            TaskWithCategory(task4, category1)
        )

        val assertList = listOf(
            TaskWithCategory(task2, null),
            TaskWithCategory(task3, null)
        )

        every { mockRepo.findAllTasksWithCategory() } returns Flowable.fromArray(mockList)

        loadCompletedTasks().subscribe(testObserver)
        testObserver.assertValue(assertList)

        verify { mockRepo.findAllTasksWithCategory() }
    }
}
