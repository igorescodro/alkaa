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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class LoadCompletedTasksByPeriodTest {

    private val taskRepository = TaskRepositoryFake()

    private val categoryRepository = CategoryRepositoryFake()

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    private val loadTrackerUseCase = LoadCompletedTasksByPeriodImpl(taskWithCategoryRepository)

    @Test
    fun test_if_completed_tasks_are_returned_in_group() = runTest {
        val category1 = Category(id = 1, name = "Category A", color = "#FFFFFF")
        val category2 = Category(id = 2, name = "Category B", color = "#CCCCCC")
        val category3 = Category(id = 3, name = "Category C", color = "#AAAAAA")
        val categoryList = listOf(category1, category2, category3)
        categoryList.forEach { categoryRepository.insertCategory(it) }

        val instant = Clock.System.now()
        val calendarIn = instant.minus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val calendarOut = instant.minus(90.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val t1 = Task(
            id = 1,
            isCompleted = false,
            title = "A",
            categoryId = 1
        )
        val t2 = Task(
            id = 2,
            isCompleted = true,
            title = "2",
            categoryId = 2,
            completedDate = calendarIn
        )
        val t3 =
            Task(
                id = 3,
                isCompleted = true,
                title = "C",
                categoryId = 2,
                completedDate = calendarIn
            )
        val t4 = Task(
            id = 4,
            isCompleted = false,
            title = "4",
            categoryId = 3
        )
        val t5 =
            Task(
                id = 5,
                isCompleted = true,
                title = "E",
                categoryId = 3,
                completedDate = calendarIn
            )
        val t6 =
            Task(
                id = 6,
                isCompleted = true,
                title = "6",
                categoryId = 3,
                completedDate = calendarOut
            )
        val taskList = listOf(t1, t2, t3, t4, t5, t6)
        taskList.forEach { task -> taskRepository.insertTask(task) }

        val trackerTasks = loadTrackerUseCase().first()

        val assertList = listOf(
            TaskWithCategory(t2, category2),
            TaskWithCategory(t3, category2),
            TaskWithCategory(t5, category3),
        )

        assertEquals(expected = assertList, actual = trackerTasks)
    }

    @Test
    fun test_if_only_completed_tasks_are_returned() = runTest {
        val category1 = Category(id = 1, name = "Category A", color = "#FFFFFF")
        val category2 = Category(id = 2, name = "Category B", color = "#CCCCCC")
        val categoryList = listOf(category1, category2)
        categoryList.forEach { categoryRepository.insertCategory(it) }

        val instant = Clock.System.now()
        val calendarIn = instant.minus(15.days).toLocalDateTime(TimeZone.currentSystemDefault())

        val t1 = Task(
            id = 1,
            isCompleted = false,
            title = "T",
            categoryId = 1
        )
        val t2 =
            Task(
                id = 2,
                isCompleted = false,
                title = "u",
                categoryId = 1,
                completedDate = calendarIn
            )
        val t3 =
            Task(
                id = 3,
                isCompleted = false,
                title = "n",
                categoryId = 1,
                completedDate = calendarIn
            )
        val t4 =
            Task(
                id = 4,
                isCompleted = true,
                title = "a",
                categoryId = 2,
                completedDate = calendarIn
            )
        val taskList = listOf(t1, t2, t3, t4)
        taskList.forEach { task -> taskRepository.insertTask(task) }

        val trackerTasks = loadTrackerUseCase().first()

        val assertList = listOf(
            TaskWithCategory(t4, category2),
        )

        assertEquals(expected = assertList, actual = trackerTasks)
    }

    @Test
    fun test_if_completed_tasks_without_category_are_considered() = runTest {
        val instant = Clock.System.now()
        val calendarIn = instant.minus(10.days).toLocalDateTime(TimeZone.currentSystemDefault())
        val task = Task(id = 3, isCompleted = true, title = "Lonely", completedDate = calendarIn)
        taskRepository.insertTask(task)

        val trackerTasks = loadTrackerUseCase().first()
        val assertList = listOf(TaskWithCategory(task, null))
        assertEquals(expected = assertList, actual = trackerTasks)
    }
}
