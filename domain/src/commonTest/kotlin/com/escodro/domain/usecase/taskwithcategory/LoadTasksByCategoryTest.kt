package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadTasksByCategoryImpl
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadUncompletedTasksImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LoadTasksByCategoryTest {

    private val taskRepository = TaskRepositoryFake()
    private val categoryRepository = CategoryRepositoryFake()
    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    private val loadUncompletedTasks = LoadUncompletedTasksImpl(taskWithCategoryRepository)
    private val loadCompletedTasks = LoadCompletedTasks(taskWithCategoryRepository)

    private val loadTasksByCategory =
        LoadTasksByCategoryImpl(loadUncompletedTasks, loadCompletedTasks)

    private val categoryId = 1L

    @BeforeTest
    fun setup() = runTest {
        categoryRepository.insertCategory(Category(id = categoryId, name = "Work", color = "#FFFFFF"))
        categoryRepository.insertCategory(Category(id = 2L, name = "Other", color = "#000000"))

        // Tasks for target category
        taskRepository.insertTask(Task(id = 1, title = "Uncompleted", isCompleted = false, categoryId = categoryId))
        taskRepository.insertTask(Task(id = 2, title = "Completed", isCompleted = true, categoryId = categoryId))
        // Task for another category — must NOT appear
        taskRepository.insertTask(Task(id = 3, title = "Other category", isCompleted = false, categoryId = 2L))
    }

    @AfterTest
    fun tearDown() = runTest {
        taskRepository.cleanTable()
        categoryRepository.cleanTable()
    }

    @Test
    fun `returns both uncompleted and completed tasks for category`() = runTest {
        val tasks = loadTasksByCategory(categoryId).first()
        assertEquals(expected = 2, actual = tasks.size)
    }

    @Test
    fun `does not include tasks from other categories`() = runTest {
        val tasks = loadTasksByCategory(categoryId).first()
        assertTrue(tasks.all { it.category?.id == categoryId })
    }

    @Test
    fun `returns tasks with correct completion states`() = runTest {
        val tasks = loadTasksByCategory(categoryId).first()
        assertTrue(tasks.any { !it.task.isCompleted })
        assertTrue(tasks.any { it.task.isCompleted })
    }
}
