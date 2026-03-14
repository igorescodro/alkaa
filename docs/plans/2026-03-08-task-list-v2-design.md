# Task List V2 — Design Document

**Date:** 2026-03-08
**Branch:** task-list-part-1
**Feature flag:** `DesignSystemConfig.IsNewDesignEnabled`

---

## Overview

A new Task List screen navigated to after a user selects a category/list. It replaces the concept
of filtering on the Home screen with a dedicated detail screen per category. The screen is gated
behind `DesignSystemConfig.IsNewDesignEnabled` and lives in a `v2` package inside `features/task`.

---

## Screen Description (top to bottom)

1. **Toolbar** — Back button. Material3 `TopAppBar` as placeholder (no custom design yet).
2. **Category header** — Placeholder emoji (📋), category name, subtitle ("14 tasks · 3 completed"),
   three-dots `IconButton` (no-op for now).
3. **Scrollable task list** — Tasks grouped into sections (hidden when empty):
   - Overdue
   - Due today
   - Upcoming
   - Completed
   - No date (tasks with no `dueDate` set)
4. **KuvioAddTaskBar** — Pinned to the bottom. New task is auto-assigned to the current category.
   `LazyColumn` has bottom `contentPadding` to prevent the bar hiding the last item.

---

## Key Decisions

| Topic | Decision |
|---|---|
| Category emoji | Placeholder `📋` for all categories (emoji field is a future domain change) |
| Task sections | Overdue → Due today → Upcoming → Completed → No date; empty sections hidden |
| Add task | Inline via KuvioAddTaskBar, auto-assigned to current category |
| Navigation destination | `TasksDestination.TaskListV2(categoryId: Long)` in `TasksDestination.kt` |
| ViewModel approach | Single ViewModel (Approach A) — loads category + all tasks together |
| V1 screen | Untouched — no regressions |

---

## Domain & Data Layer

### Reused as-is
- `LoadUncompletedTasks(categoryId: Long?)` — already supports category filter
- `LoadCategory(categoryId: Long): Category?` — loads single category
- `AddTask` — creates a new task

### Modified
- `LoadCompletedTasks` — add `categoryId: Long?` parameter for category filtering

### New
- `LoadTasksByCategory(categoryId: Long): Flow<TasksByCategory>` — combines uncompleted + completed
  tasks for a given category into a single reactive stream

### New domain model
```kotlin
data class TasksByCategory(
    val category: Category,
    val tasks: List<Task>   // all tasks (uncompleted + completed)
)
```

Grouping into time sections is a **presentation concern** handled by the ViewModel mapper.

---

## Navigation

**File:** `features/navigation-api/.../destination/TasksDestination.kt`

```kotlin
object TasksDestination {
    @Serializable
    data class TaskDetail(val taskId: Long) : Destination

    @Serializable
    data object AddTaskBottomSheet : Destination, TopAppBarVisible

    @Serializable
    data class TaskListV2(val categoryId: Long) : Destination  // new
}
```

**Nav entry** in `TaskNavGraph.kt` — guarded by `DesignSystemConfig.IsNewDesignEnabled`:
- Transition: `SlideInHorizontally` / `SlideOutHorizontally` (same as TaskDetail)
- Callbacks: `onBack`, `onTaskClick`

---

## ViewModel & State

**Location:** `features/task/.../presentation/v2/`

### View state

```kotlin
sealed class TaskListV2ViewState {
    data object Loading : TaskListV2ViewState()
    data class Error(val cause: Throwable) : TaskListV2ViewState()
    data class Loaded(
        val categoryName: String,
        val categoryEmoji: String,       // "📋" placeholder
        val totalCount: Int,
        val completedCount: Int,
        val sections: ImmutableList<TaskSection>,
        val addTaskText: String,
    ) : TaskListV2ViewState()
}

data class TaskSection(
    val type: TaskSectionType,
    val tasks: ImmutableList<TaskItem>,
)

enum class TaskSectionType { OVERDUE, TODAY, UPCOMING, COMPLETED, NO_DATE }

data class TaskItem(
    val id: Long,
    val title: String,
    val isCompleted: Boolean,
    val dueDate: LocalDateTime?,
)
```

### ViewModel responsibilities
1. Load category via `LoadCategory(categoryId)`
2. Observe tasks via `LoadTasksByCategory(categoryId)` (reactive Flow)
3. Group tasks into sections; hide empty sections
4. Compute `totalCount` and `completedCount` for subtitle
5. `addTask(title)` — calls `AddTask` with title + `categoryId` pre-set
6. `updateTaskStatus(taskId)` — calls `UpdateTaskStatus`
7. `onAddTaskTextChange(text)` — updates `addTaskText` in state

---

## Compose Screen

**Location:** `features/task/.../presentation/v2/`

```
TaskListV2Section            ← public entry point (categoryId, onBack, onTaskClick)
  └─ TaskListV2Loader        ← injects ViewModel via koinInject()
      └─ Scaffold
          ├─ topBar: TopAppBar (back button placeholder)
          ├─ bottomBar: KuvioAddTaskBar
          └─ content: Crossfade
              ├─ Loading → AlkaaLoadingContent
              ├─ Error   → DefaultIconTextContent
              ├─ Empty   → DefaultIconTextContent
              └─ Loaded  → TaskListV2Content
                  ├─ CategoryHeader
                  └─ LazyColumn (contentPadding bottom = bar height + insets)
                      └─ per visible section: stickyHeader + task rows
```

- Task rows reuse the existing `TaskItem` composable (Kuvio-styled row is future work)
- All user-facing strings live in `resources` module
- Three-dots `IconButton` fires a no-op lambda for now

---

## Testing Strategy

### PR #1 — Domain/Data (unit tests in `domain` module)
- `LoadTasksByCategoryTest` — merging uncompleted + completed tasks for a category
- `LoadCompletedTasksTest` — new `categoryId` filter correctness
- Fakes follow existing fake pattern (`LoadUncompletedTasksFake`, etc.)

### PR #2 — ViewModel (unit tests in `features/task` module)
- `TaskListV2ViewModelTest` — section grouping, empty section hiding, subtitle counts,
  add task delegation, status update delegation
- Uses `CoroutinesTestDispatcher` delegate (existing pattern)

### PR #3 — Compose screen (UI tests in `features/task` module)
- `TaskListV2ScreenTest` — fake ViewModel state, validates all view states (Loading, Error,
  Empty, Loaded with all 5 sections)

### PR #4 — E2E (instrumented tests in `shared` module)
- `TaskListV2E2ETest` — real data, full navigation flow, header correctness, sections, add task

---

## PR Plan

| PR | Scope | Compilable? |
|---|---|---|
| #1 | Domain changes + unit tests | Yes |
| #2 | ViewModel + unit tests | Yes (chained on #1) |
| #3 | Compose screen + UI tests | Yes (chained on #2) |
| #4 | E2E tests | Yes (chained on #3) |
