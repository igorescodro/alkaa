package com.escodro.domain.usecase.tracker

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import io.mockk.every
import io.mockk.mockk
import java.util.Calendar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class LoadCompletedTasksByPeriodTest {

    private val mockRepo = mockk<TaskWithCategoryRepository>(relaxed = true)

    private val completeTracker = LoadCompletedTasksByPeriod(mockRepo)

    @Test
    fun `check if completed tasks are shown in group`() = runBlockingTest {
        val category1 = Category(1, "Category A", "#FFFFFF")
        val category2 = Category(2, "Category B", "#CCCCCC")
        val category3 = Category(3, "Category C", "#AAAAAA")

        val calendarIn = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -15) }
        val calendarOut = Calendar.getInstance().apply { add(Calendar.MONTH, -3) }

        val task1 = Task(1, false, "Task 1")
        val task2 = Task(2, true, "Task 2", completedDate = calendarIn)
        val task3 = Task(3, true, "Task 3", completedDate = calendarIn)
        val task4 = Task(4, false, "Task 4")
        val task5 = Task(5, true, "Task 5", completedDate = calendarIn)
        val task6 = Task(6, true, "Task 6", completedDate = calendarOut)

        val taskList = listOf(
            TaskWithCategory(task1, category1),
            TaskWithCategory(task2, category2),
            TaskWithCategory(task3, category2),
            TaskWithCategory(task4, category3),
            TaskWithCategory(task5, category3),
            TaskWithCategory(task6, category3)
        )

        val assertList = listOf(
            TaskWithCategory(task2, category2),
            TaskWithCategory(task3, category2),
            TaskWithCategory(task5, category3)
        )

        every { mockRepo.findAllTasksWithCategory() } returns flow { emit(taskList) }

        val trackerList = completeTracker().first()
        Assert.assertEquals(assertList, trackerList)
    }

    @Test
    fun `check if only completed tasks are returned`() = runBlockingTest {
        val category1 = Category(1, "Category A", "#FFFFFF")
        val category2 = Category(2, "Category B", "#CCCCCC")

        val calendarIn = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -15) }

        val task1 = Task(1, false, "Task 1")
        val task2 = Task(2, false, "Task 2", completedDate = calendarIn)
        val task3 = Task(3, false, "Task 3", completedDate = calendarIn)
        val task4 = Task(4, true, "Task 4", completedDate = calendarIn)

        val taskList = listOf(
            TaskWithCategory(task1, category1),
            TaskWithCategory(task2, category1),
            TaskWithCategory(task3, category1),
            TaskWithCategory(task4, category2)
        )

        val assertList = listOf(
            TaskWithCategory(task4, category2)
        )

        every { mockRepo.findAllTasksWithCategory() } returns flow { emit(taskList) }

        val trackerList = completeTracker().first()
        Assert.assertEquals(assertList, trackerList)
    }

    @Test
    fun `show tasks without category`() = runBlockingTest {
        val calendarIn = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -10) }
        val task = Task(3, true, "Task 3", completedDate = calendarIn)

        val taskList = listOf(TaskWithCategory(task, null))
        every { mockRepo.findAllTasksWithCategory() } returns flow { emit(taskList) }

        val trackerList = completeTracker().first()
        Assert.assertEquals(taskList, trackerList)
    }
}
