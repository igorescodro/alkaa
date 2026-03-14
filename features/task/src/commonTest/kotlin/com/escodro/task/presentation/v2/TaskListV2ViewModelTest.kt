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
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class TaskListV2ViewModelTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadTasksByCategory = LoadTasksByCategoryFake()
    private val loadCategory = LoadCategoryFake()
    private val addTaskFake = AddTaskFake()

    private val fakeDateTimeProvider = object : DateTimeProvider {
        override fun getCurrentInstant() = kotlin.time.Instant.fromEpochMilliseconds(0)

        override fun getCurrentLocalDateTime() =
            LocalDateTime(year = 2024, month = Month.JUNE, day = 15, hour = 12, minute = 0)
    }

    private val categoryId = 1L
    private val fakeCategory = Category(id = categoryId, name = "Work", color = "#FFFFFF")
    private val taskItemMapper = TaskItemMapper()

    private val viewModel by lazy {
        TaskListV2ViewModel(
            loadTasksByCategory = loadTasksByCategory,
            loadCategory = loadCategory,
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
    }

    @Test
    fun `loading state is emitted first`() = runTest {
        // Given no special setup (loading is the initial emission)

        // When the first state is collected
        val state = viewModel.loadState(categoryId).take(1).first()

        // Then it is the loading state
        assertTrue(state is TaskListV2ViewState.Loading)
    }

    @Test
    fun `error state when category not found`() = runTest {
        // Given no category is registered for the given id

        // When the state is collected
        // Then an error state is emitted
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Error>()
            .take(1)
            .first()
    }

    @Test
    fun `error state when tasks load fails`() = runTest {
        // Given the category exists but the tasks use case throws an error
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.isErrorThrown = true

        // When the state is collected
        // Then an error state is emitted
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Error>()
            .take(1)
            .first()
    }

    @Test
    fun `loaded state shows category name`() = runTest {
        // Given the category exists with an empty task list
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(emptyList())

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the category name is shown
        assertEquals(expected = "Work", actual = loaded.categoryName)
    }

    @Test
    fun `overdue tasks grouped correctly`() = runTest {
        // Given a task with a past due date
        val pastDate = LocalDateTime(year = 2024, month = Month.JUNE, day = 10, hour = 9, minute = 0)
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(
                    task = Task(id = 1, title = "Late task", isCompleted = false, dueDate = pastDate),
                    category = fakeCategory,
                ),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the task appears in the overdue section
        val overdueSection = loaded.sections.find { it.type == TaskSectionType.OVERDUE }
        assertEquals(expected = 1, actual = overdueSection?.run { tasks.size })
    }

    @Test
    fun `today tasks grouped correctly`() = runTest {
        // Given a task due today
        val todayDate = LocalDateTime(year = 2024, month = Month.JUNE, day = 15, hour = 14, minute = 0)
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(
                    task = Task(id = 1, title = "Today task", isCompleted = false, dueDate = todayDate),
                    category = fakeCategory,
                ),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the task appears in the today section
        val todaySection = loaded.sections.find { it.type == TaskSectionType.TODAY }
        assertEquals(expected = 1, actual = todaySection?.run { tasks.size })
    }

    @Test
    fun `upcoming tasks grouped correctly`() = runTest {
        // Given a task with a future due date
        val futureDate = LocalDateTime(year = 2024, month = Month.JUNE, day = 20, hour = 9, minute = 0)
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(
                    task = Task(id = 1, title = "Future task", isCompleted = false, dueDate = futureDate),
                    category = fakeCategory,
                ),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the task appears in the upcoming section
        val upcomingSection = loaded.sections.find { it.type == TaskSectionType.UPCOMING }
        assertEquals(expected = 1, actual = upcomingSection?.run { tasks.size })
    }

    @Test
    fun `completed tasks grouped correctly`() = runTest {
        // Given a completed task
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(
                    task = Task(id = 1, title = "Done task", isCompleted = true),
                    category = fakeCategory,
                ),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the task appears in the completed section
        val completedSection = loaded.sections.find { it.type == TaskSectionType.COMPLETED }
        assertEquals(expected = 1, actual = completedSection?.run { tasks.size })
    }

    @Test
    fun `no date tasks grouped correctly`() = runTest {
        // Given an incomplete task with no due date
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(
                    task = Task(
                        id = 1,
                        title = "Someday task",
                        isCompleted = false,
                        dueDate = null,
                    ),
                    category = fakeCategory,
                ),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the task appears in the no-date section
        val noDateSection = loaded.sections.find { it.type == TaskSectionType.NO_DATE }
        assertEquals(expected = 1, actual = noDateSection?.run { tasks.size })
    }

    @Test
    fun `empty sections are not included`() = runTest {
        // Given the category exists with no tasks
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(emptyList())

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then no sections are returned
        assertTrue(loaded.sections.isEmpty())
    }

    @Test
    fun `task counts are computed correctly`() = runTest {
        // Given a mix of active and completed tasks
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(
            listOf(
                TaskWithCategory(task = Task(id = 1, title = "Active", isCompleted = false), category = fakeCategory),
                TaskWithCategory(task = Task(id = 2, title = "Done 1", isCompleted = true), category = fakeCategory),
                TaskWithCategory(task = Task(id = 3, title = "Done 2", isCompleted = true), category = fakeCategory),
            ),
        )

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the counts reflect the task list
        assertEquals(expected = 3, actual = loaded.totalCount)
        assertEquals(expected = 2, actual = loaded.completedCount)
    }

    @Test
    fun `add task submits with correct category`() = runTest {
        // Given a task title is entered
        viewModel.onAddTaskTextChange("New Task")

        // When the task is submitted
        viewModel.onAddTaskSubmit(categoryId)

        // Then the task is created with the correct category and title
        assertEquals(expected = categoryId, actual = addTaskFake.createdTask?.categoryId)
        assertEquals(expected = "New Task", actual = addTaskFake.createdTask?.title)
    }

    @Test
    fun `add task clears text after submit`() = runTest {
        // Given a task title is entered and submitted
        loadCategory.addCategory(fakeCategory)
        loadTasksByCategory.setTasks(emptyList())
        viewModel.onAddTaskTextChange("Will be cleared")
        viewModel.onAddTaskSubmit(categoryId)

        // When the loaded state is collected
        val loaded = getLoadedState()

        // Then the add-task text field is cleared
        assertEquals(expected = "", actual = loaded.addTaskText)
    }

    @Test
    fun `blank title is not submitted`() = runTest {
        // Given a blank title is entered
        viewModel.onAddTaskTextChange("   ")

        // When the task is submitted
        viewModel.onAddTaskSubmit(categoryId)

        // Then no task is created
        assertNull(addTaskFake.createdTask)
    }

    private suspend fun getLoadedState(): TaskListV2ViewState.Loaded =
        viewModel.loadState(categoryId)
            .filterIsInstance<TaskListV2ViewState.Loaded>()
            .take(1)
            .first()
}
