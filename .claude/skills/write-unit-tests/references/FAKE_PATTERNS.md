# Fake Patterns

## Fakes vs Mocks

**Always prefer fakes.** Only use mocks for types you cannot create an interface for, such as Android/Framework types (`Context`, `Resources`, etc.).

## Fake Structure

Fakes implement the domain interface, expose controllable state, and provide helper methods for assertions.

### Pattern 1: Mutation Fake with HashMap

Use when you need to assert that a method was called with specific arguments:

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

### Pattern 2: Flow-Return Fake with flowOf(list)

Use for use cases returning flows; expose state-control and cleanup methods:

```kotlin
internal class LoadUncompletedTasksFake : LoadUncompletedTasks {
    private var list: List<TaskWithCategory> = emptyList()

    fun returnValues(numberOfValues: Int) { /* build list */ }
    fun clean() { list = emptyList() }

    override fun invoke(categoryId: Long?): Flow<List<TaskWithCategory>> = flowOf(list)
}
```

### Pattern 3: Simple Nullable Field Fake

Use when the use case simply returns a value and no assertion on the call itself is needed:

```kotlin
internal class LoadTaskFake : LoadTask {
    var taskToBeReturned: Task? = null
    override suspend fun invoke(taskId: Long): Task? = taskToBeReturned
}
```

## Shared Fake Data (FAKE_* Constants)

Place `FAKE_*` constants in a `*Fake.kt` file alongside the fakes:

```kotlin
val FAKE_VIEW_TASK = ViewTask(id = 43, title = "Buy milk", dueDate = null)
val FAKE_VIEW_CATEGORY = ViewCategory(name = "Books", color = 0xFFFFFF)
val FAKE_VIEW_TASK_WITH_CATEGORY = ViewTaskWithCategory(task = FAKE_VIEW_TASK, category = FAKE_VIEW_CATEGORY)
```
