package com.escodro.category.presentation.detail

import com.escodro.category.fake.AddTaskFake
import com.escodro.category.fake.LoadCategoryFake
import com.escodro.category.fake.LoadCategoryTasksFake
import com.escodro.category.fake.UpdateTaskStatusFake
import com.escodro.category.mapper.CategoryMapper
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class CategoryDetailsViewModelTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadCategoryFake = LoadCategoryFake()
    private val loadCategoryTasksFake = LoadCategoryTasksFake()
    private val addTaskFake = AddTaskFake()
    private val updateTaskStatusFake = UpdateTaskStatusFake()
    private val mapper = CategoryDetailsMapper(CategoryMapper())
    private val appScope = AppCoroutineScope(context = testDispatcher())

    private val viewModel = CategoryDetailsViewModel(
        loadCategory = loadCategoryFake,
        loadCategoryTasks = loadCategoryTasksFake,
        addTask = addTaskFake,
        updateTaskStatus = updateTaskStatusFake,
        mapper = mapper,
        applicationScope = appScope,
    )

    @BeforeTest
    fun setup() {
        loadCategoryFake.clear()
        loadCategoryTasksFake.clear()
        addTaskFake.clear()
        updateTaskStatusFake.clear()
    }

    @Test
    fun `test if success state is emitted when category and tasks load`() = runTest {
        // Given
        loadCategoryFake.categoryToBeReturned = Category(id = 1L, name = "Work", color = "#FF0000")
        loadCategoryTasksFake.emit(emptyList())

        // When
        val state = viewModel.loadContent(categoryId = 1L).first()

        // Then
        assertIs<CategoryDetailsState.Success>(state)
    }

    @Test
    fun `test if total and completed task counts are correct`() = runTest {
        // Given
        loadCategoryFake.categoryToBeReturned = Category(id = 1L, name = "Work", color = "#FF0000")
        val completedTask = Task(id = 1L, title = "Done", isCompleted = true)
        val pendingTask = Task(id = 2L, title = "Todo")
        loadCategoryTasksFake.emit(
            listOf(
                TaskGroup.NoDueDate(tasks = listOf(pendingTask)),
                TaskGroup.Completed(tasks = listOf(completedTask)),
            ),
        )

        // When
        val state = viewModel.loadContent(categoryId = 1L).first()
        require(state is CategoryDetailsState.Success)

        // Then
        assertEquals(expected = 2, actual = state.totalTasks)
        assertEquals(expected = 1, actual = state.completedTasks)
    }

    @Test
    fun `test if adding a task assigns the correct category id`() = runTest {
        // Given
        val categoryId = 42L

        // When
        viewModel.addTask(title = "New task", dueDate = null, categoryId = categoryId)

        // Then
        assertEquals(expected = 1, actual = addTaskFake.addedTasks.size)
        assertEquals(expected = categoryId, actual = addTaskFake.addedTasks.first().categoryId)
    }

    @Test
    fun `test if blank title does not trigger add task`() = runTest {
        // Given / When
        viewModel.addTask(title = "   ", dueDate = null, categoryId = 1L)

        // Then
        assertTrue(addTaskFake.addedTasks.isEmpty())
    }

    @Test
    fun `test if update task status triggers the use case`() = runTest {
        // When
        viewModel.updateTaskStatus(taskId = 7L)

        // Then
        assertTrue(updateTaskStatusFake.updatedIds.contains(7L))
    }

    @Test
    fun `test if error state is emitted when loading fails`() = runTest {
        // Given — category not found
        loadCategoryFake.categoryToBeReturned = null
        loadCategoryTasksFake.emit(emptyList())

        // When
        val state = viewModel.loadContent(categoryId = 1L).first()

        // Then
        assertIs<CategoryDetailsState.Error>(state)
    }

    @Test
    fun `test if state re-emits after task status update`() = runTest {
        // Given — first collection with pending task
        loadCategoryFake.categoryToBeReturned = Category(id = 1L, name = "Work", color = "#FF0000")
        val task = Task(id = 1L, title = "Task 1")
        loadCategoryTasksFake.emit(listOf(TaskGroup.NoDueDate(tasks = listOf(task))))
        val firstState = viewModel.loadContent(categoryId = 1L).first()
        require(firstState is CategoryDetailsState.Success)
        assertEquals(expected = 0, actual = firstState.completedTasks)

        // When — simulate DB re-emission after task status update
        loadCategoryTasksFake.emit(
            listOf(TaskGroup.Completed(tasks = listOf(task.copy(isCompleted = true)))),
        )

        // Then — new collection reflects the updated completion state
        val newState = viewModel.loadContent(categoryId = 1L).first()
        require(newState is CategoryDetailsState.Success)
        assertEquals(expected = 1, actual = newState.completedTasks)
    }
}
