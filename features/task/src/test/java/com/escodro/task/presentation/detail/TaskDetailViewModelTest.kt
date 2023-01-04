package com.escodro.task.presentation.detail

import com.escodro.core.coroutines.AppCoroutineScope
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.main.TaskDetailState
import com.escodro.task.presentation.detail.main.TaskDetailViewModel
import com.escodro.task.presentation.detail.main.TaskId
import com.escodro.task.presentation.fake.CoroutinesDebouncerFake
import com.escodro.task.presentation.fake.FAKE_DOMAIN_TASK
import com.escodro.task.presentation.fake.LoadTaskFake
import com.escodro.task.presentation.fake.UpdateTaskCategoryFake
import com.escodro.task.presentation.fake.UpdateTaskDescriptionFake
import com.escodro.task.presentation.fake.UpdateTaskTitleFake
import com.escodro.test.rule.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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

    private val updateTaskCategory = UpdateTaskCategoryFake()

    private val taskMapper = TaskMapper(AlarmIntervalMapper())

    private val viewModel = TaskDetailViewModel(
        loadTaskUseCase = loadTask,
        updateTaskTitle = updateTaskTitle,
        updateTaskDescription = updateDescription,
        updateTaskCategory = updateTaskCategory,
        taskMapper = taskMapper,
        coroutineDebouncer = CoroutinesDebouncerFake(),
        applicationScope = AppCoroutineScope(context = coroutineTestRule.testDispatcher),
    )

    @Test
    fun `test if when a task exist it returns the success state`() = runTest {
        // Given the viewModel is called to load the task info
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        val flow = viewModel.loadTaskInfo(TaskId(FAKE_DOMAIN_TASK.id))

        // When the latest event is collected
        val state = flow.first()

        // Then the state contain the loaded task
        require(state is TaskDetailState.Loaded)
        val assertViewTask = taskMapper.toView(FAKE_DOMAIN_TASK)
        Assert.assertEquals(assertViewTask, state.task)
    }

    @Test
    fun `test if when a task does not exist it returns the error state`() = runTest {
        // Given the viewModel is called to load the task info
        loadTask.taskToBeReturned = null
        val flow = viewModel.loadTaskInfo(TaskId(FAKE_DOMAIN_TASK.id))

        // When the latest event is collected
        val state = flow.first()

        // Then the state contain the loaded task
        Assert.assertTrue(state is TaskDetailState.Error)
    }

    @Test
    fun `test if when the update title is called, the field is updated`() = runTest {
        // Given the viewModel is called to load the task info
        val taskId = TaskId(FAKE_DOMAIN_TASK.id)
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        viewModel.loadTaskInfo(taskId)

        // When the title is updated
        val newTitle = "title"
        viewModel.updateTitle(taskId = taskId, title = newTitle)

        // Then the task will be updated with given title
        Assert.assertTrue(updateTaskTitle.isTitleUpdated(FAKE_DOMAIN_TASK.id))
        val updatedTitle = updateTaskTitle.getUpdatedTitle(FAKE_DOMAIN_TASK.id)
        Assert.assertEquals(newTitle, updatedTitle)
    }

    @Test
    fun `test if when the update description is called, the field is updated`() = runTest {
        // Given the viewModel is called to load the task info
        val taskId = TaskId(FAKE_DOMAIN_TASK.id)
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        viewModel.loadTaskInfo(taskId)

        // When the description is updated
        val newDescription = "description"
        viewModel.updateDescription(taskId = taskId, description = newDescription)

        // Then the task will be updated with given description
        Assert.assertTrue(updateDescription.isDescriptionUpdated(FAKE_DOMAIN_TASK.id))
        val updatedDesc = updateDescription.getUpdatedDescription(FAKE_DOMAIN_TASK.id)
        Assert.assertEquals(newDescription, updatedDesc)
    }

    @Test
    fun `test if when update category is called, the category is updated`() = runTest {
        // Given the viewModel is called to load the categories
        val taskId = TaskId(FAKE_DOMAIN_TASK.id)
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        viewModel.loadTaskInfo(taskId)

        // When the category id is updated
        val newCategoryId = 4L
        viewModel.updateCategory(taskId = taskId, categoryId = CategoryId(newCategoryId))

        // Then the task will be updated with given category id
        Assert.assertTrue(updateTaskCategory.isCategoryUpdated(taskId.value))
        val updatedCategoryId = updateTaskCategory.getUpdatedCategory(taskId.value)
        Assert.assertEquals(newCategoryId, updatedCategoryId)
    }

    @Test
    fun `test if when update category is called with null, the category is updated`() = runTest {
        // Given the viewModel is called to load the categories
        val taskId = TaskId(FAKE_DOMAIN_TASK.id)
        loadTask.taskToBeReturned = FAKE_DOMAIN_TASK
        viewModel.loadTaskInfo(taskId)

        // When the category id is updated
        viewModel.updateCategory(taskId = taskId, categoryId = CategoryId(null))

        // Then the task will be updated with given category id
        Assert.assertTrue(updateTaskCategory.isCategoryUpdated(taskId.value))
        val updatedCategoryId = updateTaskCategory.getUpdatedCategory(taskId.value)
        Assert.assertNull(updatedCategoryId)
    }
}
