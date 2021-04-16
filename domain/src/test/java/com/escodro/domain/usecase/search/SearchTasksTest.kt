package com.escodro.domain.usecase.search

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.SearchRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.search.implementation.SearchTasksByNameImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SearchTasksTest {

    private val taskRepository = TaskRepositoryFake()

    private val searchRepository = SearchRepositoryFake(taskRepository)

    private val searchTaskUseCase = SearchTasksByNameImpl(searchRepository)

    @Before
    fun setup() = runBlockingTest {
        val task1 = Task(1, title = "Buy milk", completed = false)
        val task2 = Task(2, title = "Schedule meeting", completed = true)
        val task3 = Task(3, title = "Angela's birthday", completed = true)
        val task4 = Task(4, title = "Michael's birthday", completed = false)
        val taskList = listOf(task1, task2, task3, task4)
        taskList.forEach { task -> taskRepository.insertTask(task) }
    }

    @Test
    fun `test if search returns correct tasks`() = runBlockingTest {
        val query = "birthday"
        val taskList = searchTaskUseCase(query).first()

        Assert.assertEquals(2, taskList.size)
        Assert.assertNotEquals(taskList[0], taskList[1])
        taskList.forEach { taskWithCategory ->
            Assert.assertTrue(taskWithCategory.task.title.contains(query))
        }
    }

    @Test
    fun `test if return list is empty when query is not found`() = runBlockingTest {
        val taskList = searchTaskUseCase("pineapple")
        Assert.assertEquals(0, taskList.first().size)
    }

    @Test
    fun `test if all tasks are returned when empty query is passed`() = runBlockingTest {
        val taskList = searchTaskUseCase("")
        Assert.assertEquals(4, taskList.first().size)
    }
}
