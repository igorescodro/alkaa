---
name: write-unit-tests
description: Use when writing or modifying unit tests in the Alkaa project — triggers on tasks like "add a test", "write tests for X", "test this ViewModel", "cover this use case with tests", or "add unit test coverage".
---

# Write Unit Tests

## Overview

Unit tests in Alkaa are multiplatform-first, fake-based, and structured around a single scenario per test. Tests live in `commonTest` and run fastest via `./gradlew desktopTest`.

## Test Structure: Given / When / Then

Every test must use comments to separate the three blocks:

```kotlin
@Test
fun `test if when there are uncompleted items they are returned`() = runTest {
    // Given the use case returns a list of tasks
    val numberOfEntries = 14
    loadUncompletedTasks.returnValues(numberOfEntries)

    // When the latest state is collected
    val state = viewModel.loadTaskList().first()

    // Then the loaded state contains the correct number of items
    require(state is TaskListViewState.Loaded)
    assertEquals(expected = numberOfEntries, actual = state.items.size)
}
```

## Naming

- Use backtick-quoted, plain-language names that describe the **expected behavior**, not the method
- Start with `test if` or `check if` to read as a requirement
- Be specific about the condition and outcome

```kotlin
// ✅ Good
fun `test if when load tasks fails the error state is returned`()
fun `test if task is updated as completed`()
fun `test if category without name is not added`()

// ❌ Bad
fun testUpdateTask()
fun `update task test`()
fun test1()
```

## One Scenario Per Test

Each test covers exactly one behavior. Split side effects, error paths, and edge cases into separate tests.

```kotlin
// ✅ Good — two separate tests
fun `test if a task is updated as completed`()
fun `test if the alarm is canceled when the task is completed`()

// ❌ Bad — testing two things at once
fun `test if task is completed and alarm is canceled`()
```

## Fakes vs Mocks

**Always prefer fakes.** Only use mocks for types you cannot create an interface for, such as Android/Framework types (`Context`, `Resources`, etc.).

### Fake Structure

Fakes implement the domain interface, expose controllable state, and provide helper methods for assertions:

```kotlin
internal class UpdateTaskTitleFake : UpdateTaskTitle {
    private val updatedMap = HashMap<Long, String>()

    override suspend fun invoke(taskId: Long, title: String) {
        updatedMap[taskId] = title
    }

    // Helper for assertions
    fun isTitleUpdated(taskId: Long): Boolean = updatedMap.containsKey(taskId)
    fun getUpdatedTitle(taskId: Long): String? = updatedMap[taskId]
}
```

For use cases returning flows, expose state-control methods:

```kotlin
internal class LoadUncompletedTasksFake : LoadUncompletedTasks {
    private var list: List<TaskWithCategory> = emptyList()

    fun returnValues(numberOfValues: Int) { /* build list */ }
    fun clean() { list = emptyList() }

    override fun invoke(categoryId: Long?): Flow<List<TaskWithCategory>> = flowOf(list)
}
```

For simple return-value fakes, use a nullable field:

```kotlin
internal class LoadTaskFake : LoadTask {
    var taskToBeReturned: Task? = null
    override suspend fun invoke(taskId: Long): Task? = taskToBeReturned
}
```

### Shared Fake Data

Place `FAKE_*` constants in a `*Fake.kt` file alongside the fakes:

```kotlin
val FAKE_VIEW_TASK = ViewTask(id = 43, title = "Buy milk", dueDate = null)
val FAKE_VIEW_CATEGORY = ViewCategory(name = "Books", color = 0xFFFFFF)
val FAKE_VIEW_TASK_WITH_CATEGORY = ViewTaskWithCategory(task = FAKE_VIEW_TASK, category = FAKE_VIEW_CATEGORY)
```

## Coroutines: CoroutinesTestDispatcher

Any test class using coroutines (ViewModel tests, use case tests with `runTest`) **must** delegate `CoroutinesTestDispatcher`:

```kotlin
internal class TaskListViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadUncompletedTasks = LoadUncompletedTasksFake()

    private val viewModel = TaskListViewModel(
        loadAllTasksUseCase = loadUncompletedTasks,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
    )

    @BeforeTest
    fun setup() {
        loadUncompletedTasks.clean()
    }
}
```

- The delegation handles `Dispatchers.setMain` / `Dispatchers.resetMain` automatically via `@BeforeTest`/`@AfterTest`
- Pass `testDispatcher()` to `AppCoroutineScope` in ViewModel constructors
- Use `= runTest { }` for all suspend test bodies

## ViewModel Tests

```kotlin
internal class MyViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    // Fakes declared as fields
    private val loadItems = LoadItemsFake()
    private val updateItem = UpdateItemFake()

    // ViewModel constructed at field level using fakes
    private val viewModel = MyViewModel(
        loadItems = loadItems,
        updateItem = updateItem,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
    )

    @BeforeTest
    fun setup() {
        loadItems.clean()
        updateItem.clean()
    }

    @Test
    fun `test if loaded state is returned when items exist`() = runTest {
        // Given
        loadItems.returnValues(5)

        // When
        val state = viewModel.uiState().first()

        // Then
        require(state is MyViewState.Loaded)
        assertEquals(expected = 5, actual = state.items.size)
    }
}
```

Key points:
- ViewModel constructed at **field level** (not inside `@BeforeTest`)
- Use `flow.first()` to collect the current state
- Use `require(state is X)` for type narrowing before accessing state properties

## Use Case / Repository Tests

```kotlin
internal class AddTaskTest {
    private val taskRepository = TaskRepositoryFake()
    private val glanceInteractor = GlanceInteractorFake()

    private val addTask = AddTaskImpl(taskRepository, glanceInteractor)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        glanceInteractor.clean()
    }

    @Test
    fun `test if task is correctly added`() = runTest {
        // Given
        val task = Task(id = 1, title = "Buy milk")

        // When
        addTask(task)

        // Then
        val result = taskRepository.findTaskById(task.id)
        requireNotNull(result)
        assertEquals(expected = task.title, actual = result.title)
    }
}
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Using mocks for domain interfaces | Create a fake implementing the interface |
| Forgetting `CoroutinesTestDispatcher` delegation | Add `by CoroutinesTestDispatcherImpl()` to any class testing coroutines |
| Testing multiple scenarios in one test | Split into separate `@Test` functions |
| Skipping `@BeforeTest` cleanup | Add a `setup()` calling `clean()` on every fake |
| Constructing ViewModel in `@BeforeTest` | Construct at field level so it shares dispatcher context |
| Omitting Given/When/Then comments | Always add the three comment blocks |
| Vague test names like `testUpdate()` | Rename to describe the scenario and expected outcome |
