package com.escodro.task.presentation.list

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.presentation.detail.TaskDetailState
import com.escodro.task.presentation.detail.TaskDetailViewModel
import com.escodro.task.presentation.list.fake.FAKE_DOMAIN_TASK
import com.escodro.task.presentation.list.fake.LoadTaskFake
import com.escodro.task.presentation.list.fake.UpdateTaskFake
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskDetailViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val loadTask = LoadTaskFake()

    private val updateTask = UpdateTaskFake()

    private val viewModel = TaskDetailViewModel(
        loadTask,
        updateTask,
        TaskMapper(AlarmIntervalMapper())
    )

    @Test
    fun `test if when a task exist it returns the success state`() = runBlockingTest {
        // Given the viewModel is called to load the task info
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)

        // When the latest event is collected
        val result = arrayListOf<TaskDetailState>()
        val job = launch { viewModel.state.toList(result) }

        // Then the state contain the loaded task
        Assert.assertTrue(result.first() is TaskDetailState.Loaded)
        job.cancel()
    }

    @Test
    fun `test if when a task does not exist it returns the error state`() = runBlockingTest {
        // Given the viewModel is called to load the task info
        loadTask.taskToBeReturned = null
        viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)

        // When the latest event is collected
        val result = arrayListOf<TaskDetailState>()
        val job = launch { viewModel.state.toList(result) }

        // Then the state contain the loaded task
        Assert.assertTrue(result.first() is TaskDetailState.Error)
        job.cancel()
    }

    @Test
    fun `test if when the update title is called, the field is updated`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            // Given the viewModel is called to load the task info
            loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
            viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)

            // When the title is updated
            val newTitle = "title"
            viewModel.updateTitle(title = newTitle)
            coroutineTestRule.testDispatcher.advanceUntilIdle() /* Advance typing debounce */

            // Then the task will be updated with given title
            Assert.assertTrue(updateTask.isTaskUpdated(FAKE_DOMAIN_TASK.id))
            val updatedTask = updateTask.getUpdatedTask(FAKE_DOMAIN_TASK.id)
            require(updatedTask != null)
            Assert.assertEquals(newTitle, updatedTask.title)
        }

    @Test
    fun `test if when the update description is called, the field is updated`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            // Given the viewModel is called to load the task info
            loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
            viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)

            // When the description is updated
            val newDescription = "description"
            viewModel.updateDescription(description = newDescription)
            coroutineTestRule.testDispatcher.advanceUntilIdle() /* Advance typing debounce */

            // Then the task will be updated with given description
            Assert.assertTrue(updateTask.isTaskUpdated(FAKE_DOMAIN_TASK.id))
            val updatedTask = updateTask.getUpdatedTask(FAKE_DOMAIN_TASK.id)
            require(updatedTask != null)
            Assert.assertEquals(newDescription, updatedTask.description)
        }
}
