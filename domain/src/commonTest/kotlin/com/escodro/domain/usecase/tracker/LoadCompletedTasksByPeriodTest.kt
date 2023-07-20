package com.escodro.domain.usecase.tracker

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import com.escodro.domain.usecase.tracker.implementation.LoadCompletedTasksByPeriodImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

internal class LoadCompletedTasksByPeriodTest {

    private val taskRepository = TaskRepositoryFake()

    private val categoryRepository = CategoryRepositoryFake()

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    private val loadTrackerUseCase = LoadCompletedTasksByPeriodImpl(taskWithCategoryRepository)

    @Test
    fun `test if completed tasks are returned in group`() = runTest {
        val category1 = Category(1, "Category A", "#FFFFFF")
        val category2 = Category(2, "Category B", "#CCCCCC")
        val category3 = Category(3, "Category C", "#AAAAAA")
        val categoryList = listOf(category1, category2, category3)
        categoryRepository.insertCategory(categoryList)

        val instant = Clock.System.now()
        val calendarIn = instant.minus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val calendarOut = instant.minus(90.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val t1 = Task(1, completed = false, title = "A", categoryId = 1)
        val t2 = Task(2, completed = true, title = "2", categoryId = 2, completedDate = calendarIn)
        val t3 = Task(3, completed = true, title = "C", categoryId = 2, completedDate = calendarIn)
        val t4 = Task(4, completed = false, title = "4", categoryId = 3)
        val t5 = Task(5, completed = true, title = "E", categoryId = 3, completedDate = calendarIn)
        val t6 = Task(6, completed = true, title = "6", categoryId = 3, completedDate = calendarOut)
        val taskList = listOf(t1, t2, t3, t4, t5, t6)
        taskList.forEach { task -> taskRepository.insertTask(task) }

        val trackerTasks = loadTrackerUseCase().first()

        val assertList = listOf(
            TaskWithCategory(t2, category2),
            TaskWithCategory(t3, category2),
            TaskWithCategory(t5, category3),
        )

        assertEquals(assertList, trackerTasks)
    }

    @Test
    fun `test if only completed tasks are returned`() = runTest {
        val category1 = Category(1, "Category A", "#FFFFFF")
        val category2 = Category(2, "Category B", "#CCCCCC")
        val categoryList = listOf(category1, category2)
        categoryRepository.insertCategory(categoryList)

        val instant = Clock.System.now()
        val calendarIn = instant.minus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val t1 = Task(1, completed = false, title = "T", categoryId = 1)
        val t2 = Task(2, completed = false, title = "u", categoryId = 1, completedDate = calendarIn)
        val t3 = Task(3, completed = false, title = "n", categoryId = 1, completedDate = calendarIn)
        val t4 = Task(4, completed = true, title = "a", categoryId = 2, completedDate = calendarIn)
        val taskList = listOf(t1, t2, t3, t4)
        taskList.forEach { task -> taskRepository.insertTask(task) }

        val trackerTasks = loadTrackerUseCase().first()

        val assertList = listOf(
            TaskWithCategory(t4, category2),
        )

        assertEquals(assertList, trackerTasks)
    }

    @Test
    fun `test if completed tasks without category are considered`() = runTest {
        val instant = Clock.System.now()
        val calendarIn = instant.minus(10.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val task = Task(3, completed = true, title = "Lonely", completedDate = calendarIn)
        taskRepository.insertTask(task)

        val trackerTasks = loadTrackerUseCase().first()
        val assertList = listOf(TaskWithCategory(task, null))
        assertEquals(assertList, trackerTasks)
    }
}
