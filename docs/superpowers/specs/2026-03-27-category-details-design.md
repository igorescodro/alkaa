# Category Details Screen — Design Spec

**Date:** 2026-03-27
**Feature branch:** `feature-category-details`
**Source spec:** `docs/devspecs/category-details/CategoryDetails.md`

---

## Overview

A new full-screen Category Details screen replaces the current bottom sheet when `IsNewDesignEnabled` is `true`. It shows the category header, all associated tasks grouped by state, an empty state when there are no tasks, and a pinned Add Task bar at the bottom.

The original requirements (`CategoryDetails.md`) define four task sections: Due Today, Upcoming, No Due Date, and Completed. This design intentionally adds a fifth section — **Overdue** — for tasks whose due date has passed and which are not yet completed. This aligns with the `KuvioTaskItemState.OVERDUE` state already present in the design system and provides clearer urgency signalling to users.

---

## Approach

Extend the existing `features/category` module. No new feature module is created. Task grouping logic lives in a new domain use case. The `DateTimePicker.kt` file is promoted from `features/task` to `libraries/designsystem` so it can be shared across features. The header becomes a new Kuvio component.

---

## Section 1 — Architecture Overview

```
libraries/designsystem
  └─ components/picker/DateTimePicker.kt    (moved from features/task — file renamed, function name unchanged)
  └─ components/kuvio/header/KuvioCategoryHeader.kt  (new)

domain
  └─ model/TaskGroup.kt                     (new sealed class)
  └─ usecase/taskwithcategory/LoadCategoryTasks.kt          (new interface)
  └─ usecase/taskwithcategory/implementation/LoadCategoryTasksImpl.kt  (new)
  └─ di/DomainModule.kt                     (register LoadCategoryTasksImpl)

features/navigation-api
  └─ destination/CategoryDestination.kt     (add CategoryDetails data class)
  └─ event/CategoryEvent.kt                 (add nested OnCategoryDetailsClick; OnCategoryClick unchanged)

features/category (impl)
  └─ presentation/detail/CategoryDetailsViewModel.kt   (new)
  └─ presentation/detail/CategoryDetailsMapper.kt      (new)
  └─ presentation/detail/CategoryDetailsScreen.kt      (new)
  └─ navigation/CategoryNavGraph.kt         (update category list entry + add CategoryDetails entry)
  └─ di/CategoryModule.kt                   (register ViewModel + Mapper)
```

No changes to `KoinHelper.kt` — `domainModule` and `categoryModule` are already registered there.
No build file changes needed — `features/category/build.gradle.kts` already declares `implementation(projects.libraries.designsystem)`.

---

## Section 2 — Domain Layer

### `TaskGroup` sealed class (`domain/model/`)

```kotlin
sealed class TaskGroup {
    abstract val tasks: List<Task>

    data class Overdue(override val tasks: List<Task>) : TaskGroup()
    data class DueToday(override val tasks: List<Task>) : TaskGroup()
    data class Upcoming(override val tasks: List<Task>) : TaskGroup()
    data class NoDueDate(override val tasks: List<Task>) : TaskGroup()
    data class Completed(override val tasks: List<Task>) : TaskGroup()
}
```

### `LoadCategoryTasks` use case (`domain/usecase/taskwithcategory/`)

```kotlin
interface LoadCategoryTasks {
    operator fun invoke(categoryId: Long): Flow<List<TaskGroup>>
}
```

No `internal` modifier — domain use case interfaces are always public so they can be resolved across module boundaries by Koin.

**Implementation rules:**
- Depends on `TaskWithCategoryRepository.findAllTasksWithCategoryId(categoryId)` — already exists end-to-end, no data layer changes needed.
- Maps `TaskWithCategory` to `Task` via `.map { it.task }` before applying any grouping logic.
- Injects `DateTimeProvider` for testable date comparisons (never `Clock.System.now()` directly).
- Groups using `dateTimeProvider.getCurrentInstant().toLocalDateTime(TimeZone.UTC).date` as reference. Using `TimeZone.UTC` ensures deterministic date comparisons regardless of the CI machine's timezone. The `DateTimeProviderFake` hardcodes `1993-04-15T16:50:00Z` which maps to `1993-04-15` in UTC.
- **Grouping priority**: check `isCompleted` first — a task that is both overdue and completed belongs in `Completed`, not `Overdue`.
- Grouping rules (applied in order):
  1. `isCompleted == true` → `Completed`
  2. `dueDate == null` → `NoDueDate`
  3. `dueDate.date < today` → `Overdue`
  4. `dueDate.date == today` → `DueToday`
  5. `dueDate.date > today` → `Upcoming`
- Emits only **non-empty** groups (empty sections are not shown in UI).
- Section order in emitted list: **Overdue → DueToday → Upcoming → NoDueDate → Completed**.

**Registration:** Register as `factoryOf(::LoadCategoryTasksImpl) bind LoadCategoryTasks::class` in `domain/di/DomainModule.kt`. Koin auto-wires `TaskWithCategoryRepository` and `DateTimeProvider` from the same module.

**Data layer:** No changes needed. `findAllTasksWithCategoryId` already exists at all layers (DAO → DataSource → Repository).

**Test date anchor:** `DateTimeProviderFake` returns a hardcoded date of `1993-04-15T16:50:00Z`. All tasks in `LoadCategoryTasksTest` must be constructed with dates relative to `1993-04-15`: e.g., `dueDate = LocalDate(1993, 4, 14)` for overdue, `LocalDate(1993, 4, 15)` for due today, `LocalDate(1993, 4, 16)` for upcoming.

---

## Section 3 — Koin DI

- Register `LoadCategoryTasksImpl` in `domain/di/DomainModule.kt`: `factoryOf(::LoadCategoryTasksImpl) bind LoadCategoryTasks::class`.
- Register `CategoryDetailsViewModel` and `CategoryDetailsMapper` in `features/category/.../di/CategoryModule.kt`.
- No changes to `KoinHelper.kt` needed — `domainModule` and `categoryModule` are already registered.

---

## Section 4 — Navigation

The feature flag check (`DesignSystemConfig.IsNewDesignEnabled`) lives in `CategoryNavGraph.kt` inside `features/category`, which already depends on `libraries/designsystem`. This avoids any dependency change to `features/navigation-api`.

The NavGraph flag branch (flag-on sends `OnCategoryDetailsClick`, flag-off sends `OnCategoryClick`) is only covered at the E2E level — NavGraph entries are not unit-testable. This is intentional and consistent with the rest of the project.

`DesignSystemConfig.IsNewDesignEnabled` must be changed from `const val` to a mutable `var` to allow E2E tests to toggle it: `var IsNewDesignEnabled: Boolean = false`. Each E2E test that requires it enabled sets it to `true` in `@BeforeTest` and resets it to `false` in `@AfterTest`.

### New destination in `CategoryDestination.kt`

```kotlin
@Serializable
data class CategoryDetails(val categoryId: Long) : Destination
```

- Implements **neither** `TopLevel` nor `TopAppBarVisible` — it is a regular full-screen push destination.

### New event nested in `CategoryEvent.kt` (inside `object CategoryEvent`)

```kotlin
object CategoryEvent {
    // ... existing events unchanged ...

    data class OnCategoryDetailsClick(val categoryId: Long) : Event {
        override fun nextDestination(): Destination =
            CategoryDestination.CategoryDetails(categoryId)
    }
}
```

`OnCategoryClick` is **not modified** — it continues to route to `CategoryBottomSheet`.

### Updated category list entry in `CategoryNavGraph.kt`

```kotlin
entry<HomeDestination.CategoryList>(
    metadata = NavDisplay.transitionSpec { FadeInTransition } +
        NavDisplay.popTransitionSpec { FadeOutTransition } +
        NavDisplay.predictivePopTransitionSpec { FadeOutTransition },
) {
    CategoryListSection(
        onAddClick = {
            navEventController.sendEvent(CategoryEvent.OnNewCategoryClick)
        },
        onItemClick = { categoryId: Long? ->
            if (DesignSystemConfig.IsNewDesignEnabled && categoryId != null) {
                navEventController.sendEvent(CategoryEvent.OnCategoryDetailsClick(categoryId))
            } else {
                navEventController.sendEvent(CategoryEvent.OnCategoryClick(categoryId))
            }
        },
    )
}
```

Note: `onItemClick` receives `Long?` matching the real `CategoryListSection` signature. The null check guards against the legacy null-categoryId path.

### New entry for `CategoryDetails` in `CategoryNavGraph.kt`

```kotlin
entry<CategoryDestination.CategoryDetails> { backStackEntry ->
    val dest = backStackEntry.toRoute<CategoryDestination.CategoryDetails>()
    val isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane()
    CategoryDetailsSection(
        categoryId = dest.categoryId,
        isSinglePane = isSinglePane,
        onBackClick = { navEventController.sendEvent(Event.OnBack) },
        onTaskClick = { taskId ->
            navEventController.sendEvent(TaskEvent.OnTaskClick(id = taskId))
        },
    )
}
```

`TaskEvent.OnTaskClick(id: Long)` already exists in `features/navigation-api` — no changes needed.
`isSinglePane` is computed at the NavGraph entry, consistent with `TaskNavGraph`, `SearchNavGraph`, and `PreferenceNavGraph`.

**Navigation rules:**
- No `navEventController.sendEvent()` inside composables or ViewModels.
- Back navigation via `Event.OnBack` — never a custom event.

---

## Section 5 — Design System

### 1. Move `DateTimePicker`

Move `features/task/.../alarm/DateTimePicker.kt` → `libraries/designsystem/.../components/picker/DateTimePicker.kt`.

The composable function inside is named `DateTimerPicker` (with a trailing 'r') — this is pre-existing. The file is moved without renaming the function. Update the import in the existing task detail screen. No behavioral changes.

Note: `DateTimerPicker` retains its direct use of `Clock.System.now()` for UI-layer date initialisation. The `DateTimeProvider` injection rule applies only to domain use cases, not to Compose UI components.

### 2. New `KuvioCategoryHeader` component

**Location:** `libraries/designsystem/.../components/kuvio/header/KuvioCategoryHeader.kt`

**Signature:**
```kotlin
@Composable
fun KuvioCategoryHeader(
    name: String,
    color: Color,
    totalTasks: Int,
    completedTasks: Int,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String? = null,
)
```

The `color: androidx.compose.ui.graphics.Color` parameter receives a Compose Color converted from the view-layer `Category.color: Int` (ARGB) via `Color(category.color)`. This conversion is done in `CategoryDetailsMapper` (not in the composable). This is an intentional use of a category-specific custom color for the emoji placeholder background tint — not a theme token.

**Structure:**
- Emoji placeholder: colored rounded square (`color` at 15% alpha) with a placeholder icon. The `emoji` parameter is accepted but ignored until the `Category` domain model supports it.
- Name: `KuvioTitleLargeText`
- Progress: `KuvioBodyMediumText` — uses a format string resource in `strings.xml` (e.g., `category_header_progress` = `"%1$d tasks · %2$d completed"`) with `totalTasks` and `completedTasks` as arguments. Not a plural string — entries go in `strings.xml` only (not `plurals.xml`). Localization via `localization` skill in all four language files. Total includes completed tasks.
- Three-dot overflow icon: `KuvioIcons` vertical dots, calls `onOptionsClick` — no dropdown built yet (out of scope per spec).

**Kuvio rules:**
- Colors via `MaterialTheme.colorScheme.*` except the category tint (`color` param) which is a user-chosen custom color.
- All text via Kuvio text primitives — no raw `Text()`.
- Light + dark `@Preview` required.
- Max ~60 lines; extract private sub-composables if needed.
- All user-visible strings via `localization` skill — entries required in all four language files (en, es, fr, pt-rBR).

---

## Section 6 — Presentation

### `CategoryDetailsState` (sealed class)

```kotlin
sealed class CategoryDetailsState {
    data object Loading : CategoryDetailsState()
    data class Error(val throwable: Throwable) : CategoryDetailsState()
    data class Success(
        val category: Category,
        val categoryColor: Color,
        val groups: List<TaskGroup>,
        val totalTasks: Int,
        val completedTasks: Int,
    ) : CategoryDetailsState()
}
```

Note: `categoryColor: androidx.compose.ui.graphics.Color` is pre-converted by `CategoryDetailsMapper` (which internally uses `CategoryMapper.toView()` + `Color(viewCategory.color)`). Composables never do color conversions. `category: com.escodro.categoryapi.model.Category` (view-layer type, not domain type).

### `CategoryDetailsMapper`

Dedicated mapper class injected into the ViewModel alongside `CategoryMapper`. `TaskGroup` is a domain model used directly in the view layer. The mapper has two responsibilities:

1. **`toViewState(domainCategory: DomainCategory, groups: List<TaskGroup>): CategoryDetailsState.Success`** — where `DomainCategory` is `com.escodro.domain.model.Category` (returned by `LoadCategory`). Uses the injected `CategoryMapper.toView(domainCategory)` to convert to `com.escodro.categoryapi.model.Category` (view type, `color: Int`). Then produces `categoryColor = androidx.compose.ui.graphics.Color(viewCategory.color)`. Computes `totalTasks` and `completedTasks`. Sets `Success.category` to the view-layer `Category`. `CategoryDetailsState.Success.category` is always `com.escodro.categoryapi.model.Category`.

2. **`toTask(title: String, dueDate: LocalDateTime?, categoryId: Long): Task`** — constructs a `com.escodro.domain.model.Task` for `addTask`. Always sets `title` and `categoryId` explicitly. Relies on `Task` constructor defaults for all other fields — cross-reference `domain/model/Task.kt` directly. Never assumes a default for `categoryId` since it must always be the current screen's category.

### `CategoryDetailsViewModel`

**State exposure:** The ViewModel exposes `state: Flow<CategoryDetailsState>` built inside `loadContent(categoryId: Long)`, called by `CategoryDetailsSection`:

```kotlin
fun loadContent(categoryId: Long): Flow<CategoryDetailsState> = combine(
    flow { emit(loadCategory(categoryId)) },
    loadCategoryTasks(categoryId),
) { category, groups ->
    if (category == null) CategoryDetailsState.Error(IllegalStateException("Category not found"))
    else mapper.toViewState(category, groups)
}.catch { e ->
    emit(CategoryDetailsState.Error(e))
}
```

- `LoadCategory` returns `Category?` (suspend, not Flow) — wrap with `flow { emit(...) }` before combining.
- `.catch { e -> emit(CategoryDetailsState.Error(e)) }` handles exceptions from either upstream.
- `CategoryDetailsSection` collects state via: `val state by remember(categoryId) { viewModel.loadContent(categoryId) }.collectAsState(initial = CategoryDetailsState.Loading)`. The `remember(categoryId)` block prevents re-subscribing to the flow on every recomposition.

**Mutations:**
- `fun addTask(title: String, dueDate: LocalDateTime?)` — no-op if `title.isBlank()`. Builds a `Task` via `mapper.toTask(title, dueDate, categoryId)` then calls `AddTask`. Uses `applicationScope.launch { }`.
- `fun updateTaskStatus(taskId: Long)` — calls `UpdateTaskStatus(taskId)`. Uses `applicationScope.launch { }`.

**Constructor injections:** `LoadCategory`, `LoadCategoryTasks`, `AddTask`, `UpdateTaskStatus`, `CategoryDetailsMapper`, `AppCoroutineScope`.

`categoryId: Long` is **not** a constructor parameter. Following the `CategoryEditViewModel` pattern, `categoryId` is passed via a `loadContent(categoryId: Long)` method called from `CategoryDetailsSection` after creation. This avoids `parametersOf` in Koin, and the ViewModel is registered as `viewModelOf(::CategoryDetailsViewModel)` in `CategoryModule` — consistent with all other ViewModels in the category module.

Note: `CompleteTask` is a **concrete class** (not an interface) and cannot be faked for unit testing. `UpdateTaskStatus` (`domain/usecase/task/UpdateTaskStatus.kt`) is the interface to use — it toggles task completion state and is fully fakeable.

### Test fakes required

Create these fakes in `features/category/src/commonTest/kotlin/com/escodro/category/fake/`:

| Fake | Interface | Notes |
|------|-----------|-------|
| `AddTaskFake` | `AddTask` | Do not share from `features/task` — would create an upward dependency |
| `UpdateTaskStatusFake` | `UpdateTaskStatus` | New |
| `LoadCategoryTasksFake` | `LoadCategoryTasks` | New |

`LoadCategoryFake` already exists in `features/category/commonTest`. Add a `clear()` method: `fun clear() { categoryToBeReturned = null }`. Call `clear()` from `@BeforeTest` on every fake, matching the convention used by `AddCategoryFake` and all new fakes.

### `CategoryDetailsScreen` (three-layer pattern)

| Layer | Visibility | Responsibility |
|-------|-----------|----------------|
| `CategoryDetailsSection` | public | Injects ViewModel via `koinViewModel()`, collects `state` flow, passes to Screen |
| `CategoryDetailsScreen` | internal | Stateless; receives state + callbacks; no injection |
| `CategoryDetailsContent` | internal | Renders the actual UI; tested with Compose Testing |

**State rendering in `CategoryDetailsScreen`:**
- `Loading` → centered `CircularProgressIndicator` (or Kuvio loading composable if one exists)
- `Error` → Kuvio-styled error message with retry option (or simple error text if no retry composable exists)
- `Success` → delegates to `CategoryDetailsContent`

**UI structure of `CategoryDetailsContent`:**
- `KuvioCategoryHeader` at the top of the screen.
- `LazyColumn` with task group sections:
  - Each section: section label (`KuvioLabelMediumText`, uppercase) + task count + horizontal divider line.
  - Each task row: `KuvioTaskItem` with appropriate `KuvioTaskItemState` (`PENDING`, `COMPLETED`, `OVERDUE`). Tapping the checkbox calls `onUpdateTaskStatus(taskId)`. Tapping the row calls `onTaskClick(taskId)`.
- **Empty state**: shown when `groups` is empty — Kuvio-styled illustration + message.
- `KuvioAddTaskBar` pinned at the bottom outside the scroll area (not inside `LazyColumn`).
- `DateTimerPicker` dialog shown when user taps the calendar icon on `KuvioAddTaskBar`.

**Callbacks passed into `CategoryDetailsContent`:**
- `onAddTask(title: String, dueDate: LocalDateTime?)`
- `onUpdateTaskStatus(taskId: Long)`
- `onTaskClick(taskId: Long)`
- `onOptionsClick()`
- `onBackClick()`

**Composable rules:**
- `koinViewModel()` / `koinInject()` only inside `CategoryDetailsSection` (the Loader).
- Every rendering composable has `modifier: Modifier = Modifier`.
- All paddings are multiples of 4 dp.
- Both dark and light `@Preview` for every meaningful composable variant.
- All strings via `localization` skill — no hardcoded strings.
- No raw `Text()` — use Kuvio text primitives.

---

## Section 7 — Tests

### Unit tests — `LoadCategoryTasksTest` (`domain/commonTest`)

Using `DateTimeProviderFake` and `TaskWithCategoryRepositoryFake`:

- `` `test if overdue tasks are grouped in the overdue section` ``
- `` `test if tasks due today are grouped in the due today section` ``
- `` `test if upcoming tasks are grouped in the upcoming section` ``
- `` `test if tasks without due date are grouped in the no due date section` ``
- `` `test if completed tasks are grouped in the completed section` ``
- `` `test if completed tasks with past due date are grouped in completed not overdue` ``
- `` `test if empty sections are not included in the result` ``

### Unit tests for routing — `CategoryEventTest` (`features/category/commonTest`)

(`features/navigation-api` has no `commonTest` source set — place routing tests in `features/category/commonTest` alongside other category tests.)

- `` `test if on category details click returns category details destination` ``
- `` `test if on category click returns category bottom sheet destination` ``

### Unit tests — `CategoryDetailsViewModelTest` (`features/category/commonTest`)

Construct ViewModel at field level. `@BeforeTest` calls `clear()` on all fakes.

- `` `test if success state is emitted when category and tasks load` ``
- `` `test if total and completed task counts are correct` ``
- `` `test if adding a task assigns the correct category id` ``
- `` `test if blank title does not trigger add task` ``
- `` `test if update task status triggers the use case` ``
- `` `test if state re-emits after task status update` ``
- `` `test if error state is emitted when loading fails` ``

### UI tests — `CategoryDetailsScreenTest` (`features/category/commonTest`)

Extends `AlkaaTest()`. Snake_case naming. Each test body uses `runComposeUiTest { }`. Composable wrapped in `AlkaaThemePreview`. Strings via `runBlocking { getString(...) }`. `@AfterTest` calls `clear()` on every fake.

- `test_taskGroupsAreShown()`
- `test_emptyStateIsShownWhenNoTasks()`
- `test_correctSectionHeadersAreDisplayed()`
- `test_taskMovesToCompletedSectionOnComplete()`
- `test_clickingTaskNavigatesToTaskDetail()`

### E2E tests — `CategoryFlowTest` (`shared/commonTest`)

Extends `AlkaaTest(), KoinTest`. Data seeded via DAO in `@BeforeTest`. Entry point: `uiTest { }`. `@AfterTest` calls `afterTest()`.

- `when_category_is_clicked_and_flag_enabled_then_details_screen_is_shown()`
- `when_category_is_clicked_and_flag_disabled_then_bottom_sheet_is_shown()`
- `when_task_is_added_then_it_appears_in_the_list()`
- `when_task_with_due_date_is_added_then_it_appears_in_correct_section()`
- `when_task_is_completed_then_it_moves_to_completed_section()`

---

## Out of Scope

- Emoji field on `Category` domain model — `emoji` param accepted in `KuvioCategoryHeader` but placeholder shown only.
- Rename and Delete actions from the overflow menu — button present but no action.

---

## Acceptance Criteria

1. Given `IsNewDesignEnabled = true`, when the user clicks a category, the new screen is shown.
2. Given `IsNewDesignEnabled = false`, when the user clicks a category, the bottom sheet is shown (backward compatibility).
3. Given the user has tasks, all tasks appear in their respective state sections (Overdue → DueToday → Upcoming → NoDueDate → Completed).
4. Given the user has no tasks, an empty state message is shown.
5. Given the user adds a task, it appears in the list.
6. Given the user adds a task with a due date, it appears in the correct section.
7. Given the user completes a task, it moves to the Completed section.
