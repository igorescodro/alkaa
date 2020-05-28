package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.CategoryRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class LoadTasksWithCategoryTest {

    private val taskRepository = TaskRepositoryFake()

    private val categoryRepository = CategoryRepositoryFake()

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    private val loadCompletedTasksUseCase = LoadCompletedTasks(taskWithCategoryRepository)

    private val loadUncompletedTasksUseCase = LoadUncompletedTasks(taskWithCategoryRepository)

    @Before
    fun setup() = runBlockingTest {
        val task1 = Task(1, title = "Task 1", completed = false)
        val task2 = Task(2, title = "Task 2", completed = true)
        val task3 = Task(3, title = "Task 3", completed = true)
        val task4 = Task(4, title = "Task 4", completed = false)
        taskRepository.insertTask(task1)
        taskRepository.insertTask(task2)
        taskRepository.insertTask(task3)
        taskRepository.insertTask(task4)
    }

    @After
    fun tearDown() = runBlockingTest {
        taskRepository.cleanTable()
        categoryRepository.cleanTable()
    }

    @Test
    fun `test if completed tasks are filtered`() = runBlockingTest {
        val completedTasks = loadCompletedTasksUseCase().first()

        Assert.assertEquals(2, completedTasks.size)
        completedTasks.forEach { taskWithCategory ->
            Assert.assertTrue(taskWithCategory.task.completed)
        }
    }

    @Test
    fun `test if uncompleted tasks are filtered`() = runBlockingTest {
        val uncompletedTasks = loadUncompletedTasksUseCase().first()

        Assert.assertEquals(2, uncompletedTasks.size)
        uncompletedTasks.forEach { taskWithCategory ->
            Assert.assertFalse(taskWithCategory.task.completed)
        }
    }
}
