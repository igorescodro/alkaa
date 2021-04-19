package com.escodro.task.presentation.list

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.fake.FAKE_VIEW_TASK_WITH_CATEGORY
import com.escodro.task.presentation.fake.LoadUncompletedTasksFake
import com.escodro.task.presentation.fake.UpdateTaskStatusFake
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskListViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

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
        val flow = viewModel.loadTaskList()

        // When the latest event is collected
        val state = flow.first()

        // Then that state contains the list with uncompleted tasks
        require(state is TaskListViewState.Loaded)
        Assert.assertEquals(numberOfEntries, state.items.size)
    }

    @Test
    fun `test if when there are no uncompleted items, a empty list is returned`() =
        coroutinesRule.testDispatcher.runBlockingTest {

            // Given the use case returns an empty list
            loadUncompletedTasks.clean()
            val flow = viewModel.loadTaskList()

            // When the latest event is collected
            val state = flow.first()

            // Then that state contains the empty list
            Assert.assertTrue(state is TaskListViewState.Empty)
        }

    @Test
    fun `test if when load tasks fails, the error state is returned`() =
        coroutinesRule.testDispatcher.runBlockingTest {

            // Given the use case returns error
            loadUncompletedTasks.throwError = true
            val flow = viewModel.loadTaskList()

            // When the latest event is collected
            val state = flow.first()

            // Then that state contains the empty list
            Assert.assertTrue(state is TaskListViewState.Error)
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

    @Test
    fun `test if tasks are filtered by category when parameter is passed`() = runBlockingTest {
        // Given the use case returns the list with uncompleted tasks
        loadUncompletedTasks.returnDefaultValues()
        val flow = viewModel.loadTaskList(categoryId = 1)

        // When the latest event is collected
        val state = flow.first()

        // Then that state contains the list with uncompleted tasks
        require(state is TaskListViewState.Loaded)
        Assert.assertEquals(2, state.items.size)
    }
}
