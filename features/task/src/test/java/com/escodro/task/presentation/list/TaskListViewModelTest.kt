package com.escodro.task.presentation.list

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.fake.FAKE_VIEW_TASK_WITH_CATEGORY
import com.escodro.task.presentation.fake.LoadUncompletedTasksFake
import com.escodro.task.presentation.fake.UpdateTaskStatusFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskListViewModelTest {

    private val loadUncompletedTasks = LoadUncompletedTasksFake()

    private val updateTaskStatus = UpdateTaskStatusFake()

    private val viewModel = TaskListViewModel(
        loadUncompletedTasks,
        updateTaskStatus,
        TaskWithCategoryMapper(TaskMapper(AlarmIntervalMapper()), CategoryMapper())
    )

    @Before
    fun setup() {
        loadUncompletedTasks.clean()
    }

    @Test
    fun `test if when there are uncompleted items, they are returned`() = runBlockingTest {

        // Given the use case returns the list with uncompleted tasks
        val numberOfEntries = 14
        loadUncompletedTasks.returnValues(numberOfEntries)
        viewModel.loadTasks()

        // When the latest event is collected
        val result = arrayListOf<TaskListViewState>()
        val job = launch { viewModel.state.toList(result) }

        // Then that state contains the list with uncompleted tasks
        val state = result.first()
        require(state is TaskListViewState.Loaded)
        Assert.assertEquals(numberOfEntries, state.items.size)

        job.cancel()
    }

    @Test
    fun `test if when there are no uncompleted items, a empty list is returned`() =
        runBlockingTest {

            // Given the use case returns an empty list
            loadUncompletedTasks.clean()
            viewModel.loadTasks()

            // When the latest event is collected
            val result = arrayListOf<TaskListViewState>()
            val job = launch { viewModel.state.toList(result) }

            // Then that state contains the empty list
            Assert.assertTrue(result.first() is TaskListViewState.Empty)

            job.cancel()
        }

    @Test
    fun `test if when list changes, the state is re-triggered`() = runBlockingTest {

        // Given collecting multiple states
        val result = arrayListOf<TaskListViewState>()
        val job = launch { viewModel.state.toList(result) }

        // When multiple states are sent
        loadUncompletedTasks.clean()
        viewModel.loadTasks()
        loadUncompletedTasks.returnDefaultValues()
        viewModel.loadTasks() /* Simulate the re-trigger by flow */

        // Then multiple states are collected
        Assert.assertTrue(result.size == 2)
        Assert.assertTrue(result[0] is TaskListViewState.Empty)
        Assert.assertTrue(result[1] is TaskListViewState.Loaded)

        job.cancel()
    }

    @Test
    fun `test if when load tasks fails, the error state is returned`() = runBlockingTest {

        // Given the use case returns error
        loadUncompletedTasks.throwError = true
        viewModel.loadTasks()

        // When the latest event is collected
        val result = arrayListOf<TaskListViewState>()
        val job = launch { viewModel.state.toList(result) }

        // Then that state contains the empty list
        Assert.assertTrue(result.first() is TaskListViewState.Error)

        job.cancel()
    }

    @Test
    fun `test if task is updated`() {
        // Given a task
        val fakeTask = FAKE_VIEW_TASK_WITH_CATEGORY

        // When it calls to update the task
        viewModel.updateTaskStatus(fakeTask)

        // Then the task is updated
        Assert.assertTrue(updateTaskStatus.isTaskUpdated(fakeTask.task.id))
    }
}
