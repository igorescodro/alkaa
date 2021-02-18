package com.escodro.task.presentation.detail

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.presentation.fake.FAKE_DOMAIN_TASK
import com.escodro.task.presentation.fake.LoadTaskFake
import com.escodro.task.presentation.fake.UpdateTaskDescriptionFake
import com.escodro.task.presentation.fake.UpdateTaskTitleFake
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

    private val updateTaskTitle = UpdateTaskTitleFake()

    private val updateDescription = UpdateTaskDescriptionFake()

    private val taskMapper = TaskMapper(AlarmIntervalMapper())

    private val viewModel = TaskDetailViewModel(
        loadTaskUseCase = loadTask,
        updateTaskTitle = updateTaskTitle,
        updateTaskDescription = updateDescription,
        taskMapper = taskMapper
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
        val state = result.first()
        require(state is TaskDetailState.Loaded)
        val assertViewTask = taskMapper.toView(FAKE_DOMAIN_TASK)
        Assert.assertEquals(assertViewTask, state.task)
        job.cancel()
    }

    // @Test
    // fun `test if when there is categories created than it returns the success state with them`() =
    //     runBlockingTest {
    //         // Given the viewModel is called to load the task info
    //         loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
    //         loadAllCategories.categoriesToBeReturned = FAKE_DOMAIN_CATEGORY_LIST
    //         viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)
    //
    //         // When the latest event is collected
    //         val result = arrayListOf<TaskDetailState>()
    //         val job = launch { viewModel.state.toList(result) }
    //
    //         val state = result.first()
    //         require(state is TaskDetailState.Loaded)
    //         val assertCategoryList = categoryMapper.toView(FAKE_DOMAIN_CATEGORY_LIST)
    //         Assert.assertEquals(assertCategoryList, state.categoryList)
    //         job.cancel()
    //     }
    //
    // @Test
    // fun `test if when there is no categories created than it returns the success state with empty list`() =
    //     runBlockingTest {
    //         // Given the viewModel is called to load the task info
    //         loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
    //         loadAllCategories.categoriesToBeReturned = listOf()
    //         viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)
    //
    //         // When the latest event is collected
    //         val result = arrayListOf<TaskDetailState>()
    //         val job = launch { viewModel.state.toList(result) }
    //
    //         val state = result.first()
    //         require(state is TaskDetailState.Loaded)
    //         Assert.assertTrue(state.categoryList.isEmpty())
    //         job.cancel()
    //     }

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
            Assert.assertTrue(updateTaskTitle.isTitleUpdated(FAKE_DOMAIN_TASK.id))
            val updatedTitle = updateTaskTitle.getUpdatedTitle(FAKE_DOMAIN_TASK.id)
            Assert.assertEquals(newTitle, updatedTitle)
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
            Assert.assertTrue(updateDescription.isDescriptionUpdated(FAKE_DOMAIN_TASK.id))
            val updatedDesc = updateDescription.getUpdatedDescription(FAKE_DOMAIN_TASK.id)
            Assert.assertEquals(newDescription, updatedDesc)
        }

    // @Test
    // fun `test if when update category is called, the category is updated`() = runBlockingTest {
    //     // Given the viewModel is called to load the task info
    //     loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
    //     viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)
    //
    //     // When the category id is updated
    //     val newCategoryId = 4L
    //     viewModel.updateCategory(categoryId = newCategoryId)
    //
    //     // Then the task will be updated with given category id
    //     Assert.assertTrue(updateTask.isTaskUpdated(FAKE_DOMAIN_TASK.id))
    //     val updatedTask = updateTask.getUpdatedTask(FAKE_DOMAIN_TASK.id)
    //     require(updatedTask != null)
    //     Assert.assertEquals(newCategoryId, updatedTask.categoryId)
    // }
    //
    // @Test
    // fun `test if when update category is called with null, the category is updated`() =
    //     runBlockingTest {
    //
    //         // Given the viewModel is called to load the task info
    //         loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
    //         viewModel.setTaskInfo(FAKE_DOMAIN_TASK.id)
    //
    //         // When the category id is updated
    //         viewModel.updateCategory(categoryId = null)
    //
    //         // Then the task will be updated with given category id
    //         Assert.assertTrue(updateTask.isTaskUpdated(FAKE_DOMAIN_TASK.id))
    //         val updatedTask = updateTask.getUpdatedTask(FAKE_DOMAIN_TASK.id)
    //         require(updatedTask != null)
    //         Assert.assertNull(updatedTask.categoryId)
    //     }
}
