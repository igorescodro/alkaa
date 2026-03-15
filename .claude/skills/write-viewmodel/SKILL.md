---
name: write-viewmodel
description: Use when writing a new ViewModel or modifying an existing one in the Alkaa project. Triggers on tasks like "add a ViewModel", "create VM for X screen", "implement state handling", or "connect use case to UI".
---

# Write ViewModel

## Overview

ViewModels in Alkaa bridge domain use cases and Compose UI via cold flows and sealed state classes. They contain no business logic — only orchestration, mapping, and scope management.

## Structure

### 1. Sealed State Class

Create a dedicated sealed class in its own file (e.g., `XxxViewState.kt`):

```kotlin
internal sealed class TaskListViewState {
    data object Loading : TaskListViewState()
    data object Empty : TaskListViewState()
    data class Loaded(val items: ImmutableList<TaskWithCategory>) : TaskListViewState()
    data class Error(val cause: Throwable) : TaskListViewState()
}
```

- Use `data object` for stateless states (`Loading`, `Empty`, `Error` without payload)
- Use `data class` for states with data (`Loaded`)
- Use `ImmutableList` from `kotlinx.collections.immutable` for list payloads

### 2. Flow Exposure

Expose state as **cold `Flow<ViewState>`**, not `StateFlow`:

```kotlin
fun loadTaskList(categoryId: Long? = null): Flow<TaskListViewState> = flow {
    loadAllTasksUseCase(categoryId = categoryId)
        .map { tasks -> taskWithCategoryMapper.toView(tasks) }
        .catch { error -> emit(TaskListViewState.Error(error)) }
        .collect { tasks ->
            val state = if (tasks.isNotEmpty()) TaskListViewState.Loaded(tasks)
                        else TaskListViewState.Empty
            emit(state)
        }
}
```

Use a hot flow (`StateFlow`) only when:
1. multiple collectors must share the same stream
2. the flow modification can be triggered from multiple places

### 3. Use Case Injection

Inject use cases as constructor parameters. Call them directly — never call repositories:

```kotlin
internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask,        // ✅ use case
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val applicationScope: AppCoroutineScope,
) : ViewModel()
```

**Never inject:**
- Another ViewModel
- A repository

### 4. Coroutine Scope

| Situation | Scope |
|-----------|-------|
| Database insert / update / delete | `applicationScope.launch { }` |
| Network call that should persist | `applicationScope.launch { }` |
| Debounced UI updates | `viewModelScope` |
| Short-lived UI-bound work | `viewModelScope` |

`AppCoroutineScope` survives ViewModel destruction — use it for mutations:

```kotlin
fun updateTaskStatus(item: TaskWithCategory) = applicationScope.launch {
    updateTaskStatusUseCase(item.task.id)
}
```

### 5. Mappers

Use a dedicated mapper class — never map inline inside the ViewModel:

```kotlin
// ✅ Correct
.map { tasks -> taskWithCategoryMapper.toView(tasks) }

// ❌ Wrong — inline mapping
.map { tasks -> tasks.map { Task(id = it.id, title = it.title, ...) } }
```

Mappers are constructor-injected alongside use cases.

## Unit Tests

Use the delegation + fake pattern:

```kotlin
internal class TaskListViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadUncompletedTasks = LoadUncompletedTasksFake()
    private val mapper = TaskWithCategoryMapper(TaskMapper(AlarmIntervalMapper()), CategoryMapper())

    private val viewModel = TaskListViewModel(
        loadAllTasksUseCase = loadUncompletedTasks,
        applicationScope = AppCoroutineScope(context = testDispatcher()),
        taskWithCategoryMapper = mapper,
    )

    @Test
    fun `test if loaded state is returned when tasks exist`() = runTest {
        // Given the use case returns 5 tasks
        loadUncompletedTasks.returnValues(5)
        
        // When we load the task list
        val state = viewModel.loadTaskList().first()
        
        // Then we should get a Loaded state with 5 items
        require(state is TaskListViewState.Loaded)
        assertEquals(5, state.items.size)
    }

    @Test
    fun `test if error state is returned on failure`() = runTest {
        // Given the use case throws an error
        loadUncompletedTasks.isErrorThrown = true
        
        // When we load the task list
        val state = viewModel.loadTaskList().first()
        
        // Then we should get an Error state
        assertTrue(state is TaskListViewState.Error)
    }
}
```

**Rules:**
- Fakes only — no mocks
- `runTest` + `Flow.first()` for single-value assertions
- `AppCoroutineScope(context = testDispatcher())` to control coroutine timing
- Cover every reachable state branch: `Loading`, `Empty`, `Loaded`, `Error`

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Calling repository directly | Wrap in a use case first |
| Exposing `StateFlow` for a screen-scoped stream | Return cold `Flow<ViewState>` |
| Mapping models inline in ViewModel | Create a `XxxMapper` class |
| Using `viewModelScope` for DB writes | Use `applicationScope.launch { }` |
| Depending on another ViewModel | Extract shared logic into a use case |
| Testing with mocks | Write a `XxxFake` that implements the use case interface |
