package com.escodro.task.presentation.v2

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.task.mapper.TaskItemMapper
import com.escodro.task.presentation.fake.AddTaskFake
import com.escodro.task.presentation.fake.LoadCategoryFake
import com.escodro.task.presentation.fake.LoadTasksByCategoryFake
import com.escodro.task.presentation.fake.UpdateTaskStatusFake
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class TaskListV2ViewModelTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadTasksByCategory = LoadTasksByCategoryFake()
    private val loadCategory = LoadCategoryFake()
    private val updateTaskStatus = UpdateTaskStatusFake()
    private val addTaskFake = AddTaskFake()

    // Fixed "today" for deterministic tests: 2024-06-15
    private val fakeToday = LocalDate(2024, 6, 15)
    private val fakeDateTimeProvider = object : DateTimeProvider {
        override fun getCurrentInstant() = kotlin.time.Instant.fromEpochMilliseconds(0)
        override fun getCurrentLocalDateTime() = LocalDateTime(2024, 6, 15, 12, 0)
    }

    private val categoryId = 1L
    private val fakeCategory = Category(id = categoryId, name = "Work", color = "#FFFFFF")
    private val taskItemMapper = TaskItemMapper()

    private val viewModel by lazy {
        TaskListV2ViewModel(
            loadTasksByCategory = loadTasksByCategory,
            loadCategory = loadCategory,
            updateTaskStatus = updateTaskStatus,
            addTask = addTaskFake,
            dateTimeProvider = fakeDateTimeProvider,
            applicationScope = AppCoroutineScope(context = testDispatcher()),
            taskItemMapper = taskItemMapper,
        )
    }

    @BeforeTest
    fun setup() {
        loadTasksByCategory.clear()
        loadCategory.clear()
        addTaskFake.clear()
        loadCategory.addCategory(fakeCategory)
    }

    @Test
    fun `loading state is emitted first`() = runTest {
        val state = viewModel.loadState(categoryId).take(1).first()
        assertTrue(state is TaskListV2ViewState.Loading)
    }

    @Test
    fun `error state when category not found`() = runTest {
        loadCategory.isNull = true
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Error>()
            .take(1)
            .first() // Ensures error state is emitted
    }

    @Test
    fun `error state when tasks load fails`() = runTest {
        loadTasksByCategory.isErrorThrown = true
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Error>()
            .take(1)
            .first() // Ensures error state is emitted
    }

    @Test
    fun `loaded state shows category name`() = runTest {
        loadTasksByCategory.setTasks(emptyList())
        val loaded = getLoadedState()
        assertEquals(expected = "Work", actual = loaded.categoryName)
    }

    @Test
    fun `overdue tasks grouped correctly`() = runTest {
        val pastDate = LocalDateTime(2024, 6, 10, 9, 0) // before fakeToday
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Late task", isCompleted = false, dueDate = pastDate), fakeCategory)
        ))
        val loaded = getLoadedState()
        val overdueSection = loaded.sections.find { it.type == TaskSectionType.OVERDUE }
        assertEquals(expected = 1, actual = overdueSection?.tasks?.size)
    }

    @Test
    fun `today tasks grouped correctly`() = runTest {
        val todayDate = LocalDateTime(2024, 6, 15, 14, 0) // same date as fakeToday
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Today task", isCompleted = false, dueDate = todayDate), fakeCategory)
        ))
        val loaded = getLoadedState()
        val todaySection = loaded.sections.find { it.type == TaskSectionType.TODAY }
        assertEquals(expected = 1, actual = todaySection?.tasks?.size)
    }

    @Test
    fun `upcoming tasks grouped correctly`() = runTest {
        val futureDate = LocalDateTime(2024, 6, 20, 9, 0) // after fakeToday
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Future task", isCompleted = false, dueDate = futureDate), fakeCategory)
        ))
        val loaded = getLoadedState()
        val upcomingSection = loaded.sections.find { it.type == TaskSectionType.UPCOMING }
        assertEquals(expected = 1, actual = upcomingSection?.tasks?.size)
    }

    @Test
    fun `completed tasks grouped correctly`() = runTest {
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Done task", isCompleted = true), fakeCategory)
        ))
        val loaded = getLoadedState()
        val completedSection = loaded.sections.find { it.type == TaskSectionType.COMPLETED }
        assertEquals(expected = 1, actual = completedSection?.tasks?.size)
    }

    @Test
    fun `no date tasks grouped correctly`() = runTest {
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Someday task", isCompleted = false, dueDate = null), fakeCategory)
        ))
        val loaded = getLoadedState()
        val noDateSection = loaded.sections.find { it.type == TaskSectionType.NO_DATE }
        assertEquals(expected = 1, actual = noDateSection?.tasks?.size)
    }

    @Test
    fun `empty sections are not included`() = runTest {
        loadTasksByCategory.setTasks(emptyList())
        val loaded = getLoadedState()
        assertTrue(loaded.sections.isEmpty())
    }

    @Test
    fun `task counts are computed correctly`() = runTest {
        loadTasksByCategory.setTasks(listOf(
            TaskWithCategory(Task(id = 1, title = "Active", isCompleted = false), fakeCategory),
            TaskWithCategory(Task(id = 2, title = "Done 1", isCompleted = true), fakeCategory),
            TaskWithCategory(Task(id = 3, title = "Done 2", isCompleted = true), fakeCategory),
        ))
        val loaded = getLoadedState()
        assertEquals(expected = 3, actual = loaded.totalCount)
        assertEquals(expected = 2, actual = loaded.completedCount)
    }

    @Test
    fun `add task submits with correct category`() = runTest {
        viewModel.onAddTaskTextChange("New Task")
        viewModel.onAddTaskSubmit(categoryId)
        assertEquals(expected = categoryId, actual = addTaskFake.createdTask?.categoryId)
        assertEquals(expected = "New Task", actual = addTaskFake.createdTask?.title)
    }

    @Test
    fun `add task clears text after submit`() = runTest {
        loadTasksByCategory.setTasks(emptyList())
        viewModel.onAddTaskTextChange("Will be cleared")
        viewModel.onAddTaskSubmit(categoryId)
        val loaded = getLoadedState()
        assertEquals(expected = "", actual = loaded.addTaskText)
    }

    @Test
    fun `blank title is not submitted`() = runTest {
        viewModel.onAddTaskTextChange("   ")
        viewModel.onAddTaskSubmit(categoryId)
        assertNull(addTaskFake.createdTask)
    }

    private suspend fun getLoadedState(): TaskListV2ViewState.Loaded =
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Loaded>()
            .take(1)
            .first()
}
