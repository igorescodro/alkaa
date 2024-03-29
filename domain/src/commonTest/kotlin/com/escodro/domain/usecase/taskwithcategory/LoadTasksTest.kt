package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadUncompletedTasksImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LoadTasksTest {

    private val taskRepository = TaskRepositoryFake()

    private val categoryRepository = CategoryRepositoryFake()

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    private val loadCompletedTasksUseCase = LoadCompletedTasks(taskWithCategoryRepository)

    private val loadUncompletedTasksUseCase = LoadUncompletedTasksImpl(taskWithCategoryRepository)

    @BeforeTest
    fun setup() = runTest {
        val category1 = Category(id = 1, name = "cat1", color = "#FFFFFF")
        val category2 = Category(id = 2, name = "cat2", color = "#000000")
        val categoryList = listOf(category1, category2)
        categoryList.forEach { categoryRepository.insertCategory(it) }

        val task1 = Task(1, title = "Task 1", completed = false, categoryId = category2.id)
        val task2 = Task(2, title = "Task 2", completed = true, categoryId = category2.id)
        val task3 = Task(3, title = "Task 3", completed = true, categoryId = category1.id)
        val task4 = Task(4, title = "Task 4", completed = false)
        val taskList = listOf(task1, task2, task3, task4)
        taskList.forEach { task -> taskRepository.insertTask(task) }
    }

    @AfterTest
    fun tearDown() = runTest {
        taskRepository.cleanTable()
        categoryRepository.cleanTable()
    }

    @Test
    fun test_if_completed_tasks_are_filtered() = runTest {
        val completedTasks = loadCompletedTasksUseCase().first()

        assertEquals(2, completedTasks.size)
        completedTasks.forEach { taskWithCategory ->
            assertTrue(taskWithCategory.task.completed)
        }
    }

    @Test
    fun test_if_uncompleted_tasks_are_filtered() = runTest {
        val uncompletedTasks = loadUncompletedTasksUseCase().first()

        assertEquals(2, uncompletedTasks.size)
        uncompletedTasks.forEach { taskWithCategory ->
            assertFalse(taskWithCategory.task.completed)
        }
    }

    @Test
    fun test_if_uncompleted_tasks_are_filtered_by_category() = runTest {
        val uncompletedTasksByCategory = loadUncompletedTasksUseCase(2L).first()

        assertEquals(1, uncompletedTasksByCategory.size)
        uncompletedTasksByCategory.forEach { taskWithCategory ->
            assertFalse(taskWithCategory.task.completed)
        }
    }
}
