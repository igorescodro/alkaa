package com.escodro.domain.usecase.search

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.SearchRepositoryFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.search.implementation.SearchTasksByNameImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class SearchTasksTest {

    private val taskRepository = TaskRepositoryFake()

    private val searchRepository = SearchRepositoryFake(taskRepository)

    private val searchTaskUseCase = SearchTasksByNameImpl(searchRepository)

    @BeforeTest
    fun setup() = runTest {
        val task1 = Task(1, title = "Buy milk", isCompleted = false)
        val task2 = Task(2, title = "Schedule meeting", isCompleted = true)
        val task3 = Task(3, title = "Angela's birthday", isCompleted = true)
        val task4 = Task(4, title = "Michael's birthday", isCompleted = false)
        val taskList = listOf(task1, task2, task3, task4)
        taskList.forEach { task -> taskRepository.insertTask(task) }
    }

    @Test
    fun test_if_search_returns_correct_tasks() = runTest {
        val query = "birthday"
        val taskList = searchTaskUseCase(query).first()

        assertEquals(expected = 2, actual = taskList.size)
        assertNotEquals(illegal = taskList[0], actual = taskList[1])
        taskList.forEach { taskWithCategory ->
            assertTrue(taskWithCategory.task.title.contains(query))
        }
    }

    @Test
    fun test_if_return_list_is_empty_when_query_is_not_found() = runTest {
        val taskList = searchTaskUseCase("pineapple")
        assertEquals(expected = 0, actual = taskList.first().size)
    }

    @Test
    fun test_if_all_tasks_are_returned_when_empty_query_is_passed() = runTest {
        val taskList = searchTaskUseCase("")
        assertEquals(expected = 4, actual = taskList.first().size)
    }
}
