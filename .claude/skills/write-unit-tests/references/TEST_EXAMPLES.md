# Test Examples

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
