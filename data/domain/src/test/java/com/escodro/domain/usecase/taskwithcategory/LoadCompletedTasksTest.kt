package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class LoadCompletedTasksTest {

    private val mockRepo = mockk<TaskWithCategoryRepository>(relaxed = true)

    private val loadCompletedTasks = LoadCompletedTasks(mockRepo)

    @Test
    fun `check if repo tasks are filtered by completed`() = runBlockingTest {
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

        every { mockRepo.findAllTasksWithCategory() } returns flow { emit(mockList) }

        val completedtasks = loadCompletedTasks().first()
        Assert.assertEquals(assertList, completedtasks)

        verify { mockRepo.findAllTasksWithCategory() }
    }
}
