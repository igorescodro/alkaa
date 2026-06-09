# Category Details Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the new full-screen Category Details screen, replacing the bottom sheet when `IsNewDesignEnabled = true`, showing tasks grouped by state with an Add Task bar.

**Architecture:** Extend `features/category` with a new `detail/` presentation layer. Domain grouping logic lives in a new `LoadCategoryTasks` use case. `DateTimePicker` is promoted to `libraries/designsystem` for reuse. A new `KuvioCategoryHeader` component is added to the design system.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Koin, kotlinx.datetime, Navigation3, SQLDelight (via existing DAOs), Kotest/kotlin.test

**Spec:** `docs/superpowers/specs/2026-03-27-category-details-design.md`

---

## File Map

**New files:**
| File | Purpose |
|------|---------|
| `domain/src/commonMain/.../domain/model/TaskGroup.kt` | Sealed class grouping tasks by section |
| `domain/src/commonMain/.../domain/usecase/taskwithcategory/LoadCategoryTasks.kt` | Use case interface |
| `domain/src/commonMain/.../domain/usecase/taskwithcategory/implementation/LoadCategoryTasksImpl.kt` | Groups tasks by date/completion |
| `domain/src/commonTest/.../domain/usecase/taskwithcategory/LoadCategoryTasksTest.kt` | Unit tests for grouping logic |
| `libraries/designsystem/src/commonMain/.../designsystem/components/picker/DateTimePicker.kt` | Moved from features/task |
| `libraries/designsystem/src/commonMain/.../designsystem/components/kuvio/header/KuvioCategoryHeader.kt` | New Kuvio header component |
| `features/category/src/commonMain/.../category/presentation/detail/CategoryDetailsState.kt` | Sealed state class |
| `features/category/src/commonMain/.../category/presentation/detail/CategoryDetailsMapper.kt` | Domain→view conversion + task construction |
| `features/category/src/commonMain/.../category/presentation/detail/CategoryDetailsViewModel.kt` | Orchestrates use cases, exposes state flow |
| `features/category/src/commonMain/.../category/presentation/detail/CategoryDetailsScreen.kt` | Section + Screen + Content composables |
| `features/category/src/commonTest/.../category/fake/AddTaskFake.kt` | Fake for AddTask |
| `features/category/src/commonTest/.../category/fake/UpdateTaskStatusFake.kt` | Fake for UpdateTaskStatus |
| `features/category/src/commonTest/.../category/fake/LoadCategoryTasksFake.kt` | Fake for LoadCategoryTasks |
| `features/category/src/commonTest/.../category/presentation/detail/CategoryDetailsViewModelTest.kt` | ViewModel unit tests |
| `features/category/src/commonTest/.../category/presentation/detail/CategoryDetailsScreenTest.kt` | UI composable tests |
| `features/category/src/commonTest/.../category/event/CategoryEventTest.kt` | Routing unit tests |

**Modified files:**
| File | Change |
|------|--------|
| `domain/src/commonMain/.../domain/di/DomainModule.kt` | Register `LoadCategoryTasksImpl` |
| `libraries/designsystem/src/commonMain/.../designsystem/config/DesignSystemConfig.kt` | `const val` → `var` for testability |
| `features/navigation-api/src/commonMain/.../navigationapi/destination/CategoryDestination.kt` | Add `CategoryDetails` data class |
| `features/navigation-api/src/commonMain/.../navigationapi/event/CategoryEvent.kt` | Add `OnCategoryDetailsClick` nested class |
| `features/task/src/commonMain/.../task/presentation/detail/alarm/DateTimePicker.kt` | Delete (moved to designsystem) |
| `features/task/src/commonMain/.../task/presentation/detail/TaskDetailScreen.kt` | Update import for DateTimePicker |
| `features/category/src/commonMain/.../category/navigation/CategoryNavGraph.kt` | Feature flag branch + new entry |
| `features/category/src/commonMain/.../category/di/CategoryModule.kt` | Register ViewModel + Mapper |
| `features/category/src/commonTest/.../category/fake/LoadCategoryFake.kt` | Add `clear()` method |
| `resources/commonMain/composeResources/values/strings.xml` | Add `category_header_progress` string |
| `resources/commonMain/composeResources/values-es/strings.xml` | Spanish translation |
| `resources/commonMain/composeResources/values-fr/strings.xml` | French translation |
| `resources/commonMain/composeResources/values-pt-rBR/strings.xml` | Portuguese translation |
| `shared/src/commonTest/.../alkaa/CategoryFlowTest.kt` | Add E2E tests |

---

## Task 1: Domain model — `TaskGroup`

**Files:**
- Create: `domain/src/commonMain/kotlin/com/escodro/domain/model/TaskGroup.kt`

- [ ] **Create `TaskGroup.kt`**

```kotlin
package com.escodro.domain.model

sealed class TaskGroup {
    abstract val tasks: List<Task>

    data class Overdue(override val tasks: List<Task>) : TaskGroup()
    data class DueToday(override val tasks: List<Task>) : TaskGroup()
    data class Upcoming(override val tasks: List<Task>) : TaskGroup()
    data class NoDueDate(override val tasks: List<Task>) : TaskGroup()
    data class Completed(override val tasks: List<Task>) : TaskGroup()
}
```

- [ ] **Run desktop tests to verify no regressions**

```bash
./gradlew :domain:desktopTest
```
Expected: All existing tests pass.

- [ ] **Commit**

```bash
git add domain/src/commonMain/kotlin/com/escodro/domain/model/TaskGroup.kt
git commit -m "feat: add TaskGroup domain model for category task sections"
```

---

## Task 2: Domain use case — `LoadCategoryTasks` interface + tests

**Files:**
- Create: `domain/src/commonMain/kotlin/com/escodro/domain/usecase/taskwithcategory/LoadCategoryTasks.kt`
- Create: `domain/src/commonTest/kotlin/com/escodro/domain/usecase/taskwithcategory/LoadCategoryTasksTest.kt`

- [ ] **Create the interface**

```kotlin
package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskGroup
import kotlinx.coroutines.flow.Flow

interface LoadCategoryTasks {
    operator fun invoke(categoryId: Long): Flow<List<TaskGroup>>
}
```

- [ ] **Write the failing tests**

Reference: `DateTimeProviderFake` hardcodes `1993-04-15T16:50:00Z` (UTC). All test tasks use dates relative to `1993-04-15`.

```kotlin
package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadCategoryTasksImpl
import com.escodro.test.CoroutinesTestDispatcher
import com.escodro.test.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LoadCategoryTasksTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val repository = TaskWithCategoryRepositoryFake()
    private val dateProvider = DateTimeProviderFake()
    private val useCase = LoadCategoryTasksImpl(
        repository = repository,
        dateTimeProvider = dateProvider,
    )

    @BeforeTest
    fun setup() {
        repository.clear()
    }

    // Overdue = due date before 1993-04-15
    @Test
    fun `test if overdue tasks are grouped in the overdue section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(1993, 4, 14))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Overdue })
        assertEquals(1, groups.filterIsInstance<TaskGroup.Overdue>().first().tasks.size)
    }

    @Test
    fun `test if tasks due today are grouped in the due today section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(1993, 4, 15))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.DueToday })
    }

    @Test
    fun `test if upcoming tasks are grouped in the upcoming section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(1993, 4, 16))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Upcoming })
    }

    @Test
    fun `test if tasks without due date are grouped in the no due date section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = null)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.NoDueDate })
    }

    @Test
    fun `test if completed tasks are grouped in the completed section`() = runTest {
        // Given
        val task = buildTask(id = 1L, isCompleted = true)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Completed })
    }

    @Test
    fun `test if completed tasks with past due date are grouped in completed not overdue`() = runTest {
        // Given — task is both overdue AND completed; should go to Completed
        val task = buildTask(id = 1L, dueDate = LocalDate(1993, 4, 14), isCompleted = true)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Completed })
        assertTrue(groups.none { it is TaskGroup.Overdue })
    }

    @Test
    fun `test if empty sections are not included in the result`() = runTest {
        // Given — one overdue task only
        val task = buildTask(id = 1L, dueDate = LocalDate(1993, 4, 14))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then — only Overdue is emitted; other sections absent
        assertEquals(1, groups.size)
        assertTrue(groups.first() is TaskGroup.Overdue)
    }

    // Helpers
    private fun buildTask(
        id: Long,
        dueDate: LocalDate? = null,
        isCompleted: Boolean = false,
    ) = Task(
        id = id,
        title = "Task $id",
        categoryId = 1L,
        isCompleted = isCompleted,
        dueDate = dueDate?.let { LocalDateTime(it, LocalTime(9, 0)) },
    )

    private fun buildTaskWithCategory(task: Task) =
        TaskWithCategory(task = task, category = null)
}
```

- [ ] **Run tests to verify they fail (class does not exist yet)**

```bash
./gradlew :domain:desktopTest --tests "*.LoadCategoryTasksTest"
```
Expected: Compilation failure — `LoadCategoryTasksImpl` not found.

- [ ] **Create `LoadCategoryTasksImpl`**

```kotlin
package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class LoadCategoryTasksImpl(
    private val repository: TaskWithCategoryRepository,
    private val dateTimeProvider: DateTimeProvider,
) : LoadCategoryTasks {

    override fun invoke(categoryId: Long): Flow<List<TaskGroup>> =
        repository.findAllTasksWithCategoryId(categoryId).map { list ->
            val tasks = list.map { it.task }
            val today = dateTimeProvider.getCurrentInstant()
                .toLocalDateTime(TimeZone.UTC).date
            buildGroups(tasks, today)
        }

    private fun buildGroups(tasks: List<Task>, today: kotlinx.datetime.LocalDate): List<TaskGroup> {
        val completed = tasks.filter { it.isCompleted }
        val active = tasks.filter { !it.isCompleted }

        val noDueDate = active.filter { it.dueDate == null }
        val overdue = active.filter { it.dueDate != null && it.dueDate.date < today }
        val dueToday = active.filter { it.dueDate != null && it.dueDate.date == today }
        val upcoming = active.filter { it.dueDate != null && it.dueDate.date > today }

        return listOfNotNull(
            TaskGroup.Overdue(overdue).takeIf { overdue.isNotEmpty() },
            TaskGroup.DueToday(dueToday).takeIf { dueToday.isNotEmpty() },
            TaskGroup.Upcoming(upcoming).takeIf { upcoming.isNotEmpty() },
            TaskGroup.NoDueDate(noDueDate).takeIf { noDueDate.isNotEmpty() },
            TaskGroup.Completed(completed).takeIf { completed.isNotEmpty() },
        )
    }
}
```

- [ ] **Run tests to verify they pass**

```bash
./gradlew :domain:desktopTest --tests "*.LoadCategoryTasksTest"
```
Expected: All 7 tests PASS.

- [ ] **Register in `DomainModule.kt`**

In `domain/src/commonMain/kotlin/com/escodro/domain/di/DomainModule.kt`, add inside the module block alongside other `taskwithcategory` use cases:

```kotlin
factoryOf(::LoadCategoryTasksImpl) bind LoadCategoryTasks::class
```

Also add the import at the top:
```kotlin
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadCategoryTasksImpl
```

- [ ] **Run all domain tests**

```bash
./gradlew :domain:desktopTest
```
Expected: All pass.

- [ ] **Commit**

```bash
git add domain/src/
git commit -m "feat: add LoadCategoryTasks use case with section grouping logic"
```

---

## Task 3: Navigation — destination, event, feature flag

**Files:**
- Modify: `features/navigation-api/src/commonMain/kotlin/com/escodro/navigationapi/destination/CategoryDestination.kt`
- Modify: `features/navigation-api/src/commonMain/kotlin/com/escodro/navigationapi/event/CategoryEvent.kt`
- Modify: `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/config/DesignSystemConfig.kt`
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/event/CategoryEventTest.kt`

- [ ] **Add `CategoryDetails` to `CategoryDestination.kt`**

```kotlin
@Serializable
data class CategoryDetails(val categoryId: Long) : Destination
```

Add alongside `CategoryBottomSheet`. Implements neither `TopLevel` nor `TopAppBarVisible`.

- [ ] **Add `OnCategoryDetailsClick` to `CategoryEvent.kt`** (inside the existing `object CategoryEvent`)

```kotlin
data class OnCategoryDetailsClick(val categoryId: Long) : Event {
    override fun nextDestination(): Destination =
        CategoryDestination.CategoryDetails(categoryId)
}
```

- [ ] **Change `IsNewDesignEnabled` from `const val` to `var` in `DesignSystemConfig.kt`**

```kotlin
object DesignSystemConfig {
    var IsNewDesignEnabled: Boolean = false
}
```

- [ ] **Write routing tests**

```kotlin
package com.escodro.category.event

import com.escodro.navigationapi.destination.CategoryDestination
import com.escodro.navigationapi.event.CategoryEvent
import kotlin.test.Test
import kotlin.test.assertIs

internal class CategoryEventTest {

    @Test
    fun `test if on category details click returns category details destination`() {
        // Given
        val event = CategoryEvent.OnCategoryDetailsClick(categoryId = 42L)

        // When
        val destination = event.nextDestination()

        // Then
        assertIs<CategoryDestination.CategoryDetails>(destination)
    }

    @Test
    fun `test if on category click returns category bottom sheet destination`() {
        // Given
        val event = CategoryEvent.OnCategoryClick(categoryId = 42L)

        // When
        val destination = event.nextDestination()

        // Then
        assertIs<CategoryDestination.CategoryBottomSheet>(destination)
    }
}
```

- [ ] **Run routing tests**

```bash
./gradlew :features:category:desktopTest --tests "*.CategoryEventTest"
```
Expected: Both tests PASS.

- [ ] **Commit**

```bash
git add features/navigation-api/src/ libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/config/ features/category/src/commonTest/kotlin/com/escodro/category/event/
git commit -m "feat: add CategoryDetails destination, routing event, and testable feature flag"
```

---

## Task 4: Move `DateTimePicker` to design system

**Files:**
- Create: `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/picker/DateTimePicker.kt`
- Delete: `features/task/src/commonMain/kotlin/com/escodro/task/presentation/detail/alarm/DateTimePicker.kt`
- Modify: `features/task/src/commonMain/kotlin/com/escodro/task/presentation/detail/TaskDetailScreen.kt` (or wherever `DateTimerPicker` is imported)

- [ ] **Copy `DateTimePicker.kt` to the new location**

Copy the full file content from `features/task/.../alarm/DateTimePicker.kt` to `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/picker/DateTimePicker.kt`. Change only the package declaration:

```kotlin
package com.escodro.designsystem.components.picker
```

All other content — including the `DateTimerPicker` function name — remains unchanged.

- [ ] **Delete the original file**

```bash
git rm features/task/src/commonMain/kotlin/com/escodro/task/presentation/detail/alarm/DateTimePicker.kt
```

- [ ] **Update the import in the task detail screen**

Find the import `import com.escodro.task.presentation.detail.alarm.DateTimerPicker` in task feature files and replace with:

```kotlin
import com.escodro.designsystem.components.picker.DateTimerPicker
```

Run a search first:
```bash
grep -r "DateTimerPicker" features/task/src/commonMain/ --include="*.kt" -l
```

- [ ] **Build to verify no broken imports**

```bash
./gradlew :features:task:assemble :libraries:designsystem:assemble
```
Expected: Compiles cleanly.

- [ ] **Commit**

```bash
git add libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/picker/
git add features/task/src/
git commit -m "refactor: promote DateTimePicker to designsystem for cross-feature reuse"
```

---

## Task 5: Localization strings for `KuvioCategoryHeader`

**Files:**
- Modify: `resources/commonMain/composeResources/values/strings.xml`
- Modify: `resources/commonMain/composeResources/values-es/strings.xml`
- Modify: `resources/commonMain/composeResources/values-fr/strings.xml`
- Modify: `resources/commonMain/composeResources/values-pt-rBR/strings.xml`

Follow the `localization` skill for conventions. Key: `category_header_progress` uses `%1$d` and `%2$d` for total and completed counts.

- [ ] **Add to `values/strings.xml`**

```xml
<string name="category_header_progress">%1$d tasks · %2$d completed</string>
<string name="category_details_empty_title">No tasks yet</string>
<string name="category_details_empty_description">Add a task using the bar below</string>
<string name="category_details_options_content_description">Category options</string>
<string name="category_details_section_overdue">Overdue</string>
<string name="category_details_section_due_today">Due Today</string>
<string name="category_details_section_upcoming">Upcoming</string>
<string name="category_details_section_no_due_date">No Due Date</string>
<string name="category_details_section_completed">Completed</string>
```

- [ ] **Add translations to `values-es/strings.xml`**

```xml
<string name="category_header_progress">%1$d tareas · %2$d completadas</string>
<string name="category_details_empty_title">Sin tareas</string>
<string name="category_details_empty_description">Agrega una tarea usando la barra de abajo</string>
<string name="category_details_options_content_description">Opciones de categoría</string>
<string name="category_details_section_overdue">Atrasadas</string>
<string name="category_details_section_due_today">Para hoy</string>
<string name="category_details_section_upcoming">Próximas</string>
<string name="category_details_section_no_due_date">Sin fecha</string>
<string name="category_details_section_completed">Completadas</string>
```

- [ ] **Add translations to `values-fr/strings.xml`**

```xml
<string name="category_header_progress">%1$d tâches · %2$d terminées</string>
<string name="category_details_empty_title">Aucune tâche</string>
<string name="category_details_empty_description">Ajoutez une tâche en utilisant la barre ci-dessous</string>
<string name="category_details_options_content_description">Options de catégorie</string>
<string name="category_details_section_overdue">En retard</string>
<string name="category_details_section_due_today">Pour aujourd\'hui</string>
<string name="category_details_section_upcoming">À venir</string>
<string name="category_details_section_no_due_date">Sans date</string>
<string name="category_details_section_completed">Terminées</string>
```

- [ ] **Add translations to `values-pt-rBR/strings.xml`**

```xml
<string name="category_header_progress">%1$d tarefas · %2$d concluídas</string>
<string name="category_details_empty_title">Nenhuma tarefa</string>
<string name="category_details_empty_description">Adicione uma tarefa usando a barra abaixo</string>
<string name="category_details_options_content_description">Opções de categoria</string>
<string name="category_details_section_overdue">Atrasadas</string>
<string name="category_details_section_due_today">Para hoje</string>
<string name="category_details_section_upcoming">Próximas</string>
<string name="category_details_section_no_due_date">Sem data</string>
<string name="category_details_section_completed">Concluídas</string>
```

- [ ] **Commit**

```bash
git add resources/
git commit -m "feat: add CategoryDetails localization strings"
```

---

## Task 6: New `KuvioCategoryHeader` design system component

**Files:**
- Create: `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/header/KuvioCategoryHeader.kt`

Reference existing Kuvio components (e.g., `KuvioAddTaskBar.kt`, `KuvioTaskItem.kt`) for style patterns. Max ~60 lines. No raw `Text()` or hardcoded colors.

- [ ] **Implement `KuvioCategoryHeader.kt`**

```kotlin
package com.escodro.designsystem.components.kuvio.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.icon.placeholder.KuvioPlaceholderIcon
import com.escodro.designsystem.components.kuvio.text.body.KuvioBodyMediumText
import com.escodro.designsystem.components.kuvio.text.title.KuvioTitleLargeText
import com.escodro.designsystem.icons.KuvioIcons
import com.escodro.resources.Res
import com.escodro.resources.category_details_options_content_description
import com.escodro.resources.category_header_progress
import org.jetbrains.compose.resources.stringResource

@Composable
fun KuvioCategoryHeader(
    name: String,
    color: Color,
    totalTasks: Int,
    completedTasks: Int,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryEmojiBox(color = color)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            KuvioTitleLargeText(text = name)
            KuvioBodyMediumText(
                text = stringResource(Res.string.category_header_progress, totalTasks, completedTasks),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onOptionsClick) {
            Icon(
                imageVector = KuvioIcons.MoreVert,
                contentDescription = stringResource(Res.string.category_details_options_content_description),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun CategoryEmojiBox(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        KuvioPlaceholderIcon(tint = color)
    }
}
```

> Note: If `KuvioPlaceholderIcon` does not exist, use any existing icon from `KuvioIcons` as the placeholder, or a simple `Box` with the first letter of the category name.

- [ ] **Add light and dark previews** (at the bottom of the file)

```kotlin
// Preview imports omitted for brevity — follow the pattern in KuvioAddTaskBar.kt
@Preview
@Composable
private fun KuvioCategoryHeaderPreview() {
    AlkaaThemePreview {
        KuvioCategoryHeader(
            name = "Work",
            color = Color(0xFF6200EA),
            totalTasks = 14,
            completedTasks = 3,
            onOptionsClick = {},
        )
    }
}

@Preview
@Composable
private fun KuvioCategoryHeaderDarkPreview() {
    AlkaaThemePreview(darkTheme = true) {
        KuvioCategoryHeader(
            name = "Work",
            color = Color(0xFF6200EA),
            totalTasks = 14,
            completedTasks = 3,
            onOptionsClick = {},
        )
    }
}
```

- [ ] **Build design system to verify it compiles**

```bash
./gradlew :libraries:designsystem:assemble
```
Expected: Compiles cleanly.

- [ ] **Commit**

```bash
git add libraries/designsystem/src/
git commit -m "feat: add KuvioCategoryHeader design system component"
```

---

## Task 7: Presentation state, mapper, and fakes

**Files:**
- Create: `features/category/src/commonMain/kotlin/com/escodro/category/presentation/detail/CategoryDetailsState.kt`
- Create: `features/category/src/commonMain/kotlin/com/escodro/category/presentation/detail/CategoryDetailsMapper.kt`
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/fake/AddTaskFake.kt`
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/fake/UpdateTaskStatusFake.kt`
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/fake/LoadCategoryTasksFake.kt`
- Modify: `features/category/src/commonTest/kotlin/com/escodro/category/fake/LoadCategoryFake.kt`

- [ ] **Create `CategoryDetailsState.kt`**

```kotlin
package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.model.Category
import com.escodro.domain.model.TaskGroup

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

- [ ] **Create `CategoryDetailsMapper.kt`**

```kotlin
package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import com.escodro.category.mapper.CategoryMapper
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import kotlinx.datetime.LocalDateTime
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryDetailsMapper(
    private val categoryMapper: CategoryMapper,
) {

    fun toViewState(
        domainCategory: DomainCategory,
        groups: List<TaskGroup>,
    ): CategoryDetailsState.Success {
        val viewCategory = categoryMapper.toView(domainCategory)
        val allTasks = groups.flatMap { it.tasks }
        return CategoryDetailsState.Success(
            category = viewCategory,
            categoryColor = Color(viewCategory.color),
            groups = groups,
            totalTasks = allTasks.size,
            completedTasks = groups.filterIsInstance<TaskGroup.Completed>()
                .sumOf { it.tasks.size },
        )
    }

    fun toTask(
        title: String,
        dueDate: LocalDateTime?,
        categoryId: Long,
    ): Task = Task(
        title = title,
        dueDate = dueDate,
        categoryId = categoryId,
    )
}
```

- [ ] **Add `clear()` to `LoadCategoryFake.kt`**

Find the `var categoryToBeReturned` field and add:
```kotlin
fun clear() {
    categoryToBeReturned = null
}
```

- [ ] **Create `AddTaskFake.kt`**

```kotlin
package com.escodro.category.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask

internal class AddTaskFake : AddTask {
    val addedTasks = mutableListOf<Task>()

    override suspend fun invoke(task: Task) {
        addedTasks.add(task)
    }

    fun clear() {
        addedTasks.clear()
    }
}
```

- [ ] **Create `UpdateTaskStatusFake.kt`**

```kotlin
package com.escodro.category.fake

import com.escodro.domain.usecase.task.UpdateTaskStatus

internal class UpdateTaskStatusFake : UpdateTaskStatus {
    val updatedIds = mutableListOf<Long>()

    override suspend fun invoke(taskId: Long) {
        updatedIds.add(taskId)
    }

    fun clear() {
        updatedIds.clear()
    }
}
```

- [ ] **Create `LoadCategoryTasksFake.kt`**

```kotlin
package com.escodro.category.fake

import com.escodro.domain.model.TaskGroup
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class LoadCategoryTasksFake : LoadCategoryTasks {
    private val flow = MutableStateFlow<List<TaskGroup>>(emptyList())

    fun emit(groups: List<TaskGroup>) {
        flow.value = groups
    }

    override fun invoke(categoryId: Long): Flow<List<TaskGroup>> = flow

    fun clear() {
        flow.value = emptyList()
    }
}
```

- [ ] **Build to verify compilation**

```bash
./gradlew :features:category:assemble
```

- [ ] **Commit**

```bash
git add features/category/src/
git commit -m "feat: add CategoryDetails state, mapper, and test fakes"
```

---

## Task 8: `CategoryDetailsViewModel` with TDD

**Files:**
- Create: `features/category/src/commonMain/kotlin/com/escodro/category/presentation/detail/CategoryDetailsViewModel.kt`
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/presentation/detail/CategoryDetailsViewModelTest.kt`

- [ ] **Write failing tests first**

```kotlin
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
import com.escodro.test.CoroutinesTestDispatcher
import com.escodro.test.CoroutinesTestDispatcherImpl
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
            )
        )

        // When
        val state = viewModel.loadContent(categoryId = 1L).first()
        require(state is CategoryDetailsState.Success)

        // Then
        assertEquals(2, state.totalTasks)
        assertEquals(1, state.completedTasks)
    }

    @Test
    fun `test if adding a task assigns the correct category id`() = runTest {
        // Given
        val categoryId = 42L

        // When
        viewModel.addTask(title = "New task", dueDate = null, categoryId = categoryId)
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(1, addTaskFake.addedTasks.size)
        assertEquals(categoryId, addTaskFake.addedTasks.first().categoryId)
    }

    @Test
    fun `test if blank title does not trigger add task`() = runTest {
        // Given / When
        viewModel.addTask(title = "   ", dueDate = null, categoryId = 1L)
        testScheduler.advanceUntilIdle()

        // Then
        assertTrue(addTaskFake.addedTasks.isEmpty())
    }

    @Test
    fun `test if update task status triggers the use case`() = runTest {
        // When
        viewModel.updateTaskStatus(taskId = 7L)
        testScheduler.advanceUntilIdle()

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
        // Given
        loadCategoryFake.categoryToBeReturned = Category(id = 1L, name = "Work", color = "#FF0000")
        val task = Task(id = 1L, title = "Task 1")
        loadCategoryTasksFake.emit(listOf(TaskGroup.NoDueDate(tasks = listOf(task))))
        val flow = viewModel.loadContent(categoryId = 1L)
        require(flow.first() is CategoryDetailsState.Success)

        // When — trigger status update, then simulate DB re-emission
        viewModel.updateTaskStatus(taskId = 1L)
        testScheduler.advanceUntilIdle()
        loadCategoryTasksFake.emit(
            listOf(TaskGroup.Completed(tasks = listOf(task.copy(isCompleted = true))))
        )

        // Then — updated state reflects the completion
        val newState = flow.first()
        require(newState is CategoryDetailsState.Success)
        assertEquals(1, newState.completedTasks)
    }
}
```

> Note: `AppCoroutineScope(context = testDispatcher())` creates a real scope bound to the test dispatcher. Use `testScheduler.advanceUntilIdle()` (from `CoroutinesTestDispatcher` delegate) to drain pending coroutines instead of relying on scope-level helpers.

- [ ] **Run tests to confirm they fail**

```bash
./gradlew :features:category:desktopTest --tests "*.CategoryDetailsViewModelTest"
```
Expected: Compilation failure.

- [ ] **Create `CategoryDetailsViewModel.kt`**

```kotlin
package com.escodro.category.presentation.detail

import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

internal class CategoryDetailsViewModel(
    private val loadCategory: LoadCategory,
    private val loadCategoryTasks: LoadCategoryTasks,
    private val addTask: AddTask,
    private val updateTaskStatus: UpdateTaskStatus,
    private val mapper: CategoryDetailsMapper,
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    fun loadContent(categoryId: Long): Flow<CategoryDetailsState> = combine(
        flow { emit(loadCategory(categoryId)) },
        loadCategoryTasks(categoryId),
    ) { category, groups ->
        if (category == null) {
            CategoryDetailsState.Error(IllegalStateException("Category not found"))
        } else {
            mapper.toViewState(category, groups)
        }
    }.catch { e ->
        emit(CategoryDetailsState.Error(e))
    }

    fun addTask(title: String, dueDate: LocalDateTime?, categoryId: Long) {
        if (title.isBlank()) return
        applicationScope.launch {
            addTask(mapper.toTask(title, dueDate, categoryId))
        }
    }

    fun updateTaskStatus(taskId: Long) {
        applicationScope.launch {
            updateTaskStatus(taskId)
        }
    }
}
```

> Note: The base class is `androidx.lifecycle.ViewModel` (confirmed from `CategoryEditViewModel`'s imports).

- [ ] **Run ViewModel tests**

```bash
./gradlew :features:category:desktopTest --tests "*.CategoryDetailsViewModelTest"
```
Expected: All tests PASS.

- [ ] **Register in `CategoryModule.kt`**

Add to `features/category/src/commonMain/kotlin/com/escodro/category/di/CategoryModule.kt`:

```kotlin
viewModelOf(::CategoryDetailsViewModel)
factoryOf(::CategoryDetailsMapper)
```

Add the imports:
```kotlin
import com.escodro.category.presentation.detail.CategoryDetailsMapper
import com.escodro.category.presentation.detail.CategoryDetailsViewModel
```

- [ ] **Commit**

```bash
git add features/category/src/
git commit -m "feat: add CategoryDetailsViewModel with state flow and task mutations"
```

---

## Task 9: `CategoryDetailsScreen` composables

**Files:**
- Create: `features/category/src/commonMain/kotlin/com/escodro/category/presentation/detail/CategoryDetailsScreen.kt`

The screen follows the three-layer pattern: `CategoryDetailsSection` (Loader, public) → `CategoryDetailsScreen` (stateless, internal) → `CategoryDetailsContent` (rendering, internal).

- [ ] **Implement `CategoryDetailsScreen.kt`**

```kotlin
package com.escodro.category.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.components.kuvio.bar.KuvioAddTaskBar
import com.escodro.designsystem.components.kuvio.header.KuvioCategoryHeader
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItem
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemData
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemState
import com.escodro.designsystem.components.kuvio.text.label.KuvioLabelMediumText
import com.escodro.designsystem.components.picker.DateTimerPicker
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import kotlinx.datetime.LocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color

// ── Section (Loader) ──────────────────────────────────────────────────────────

@Composable
fun CategoryDetailsSection(
    categoryId: Long,
    isSinglePane: Boolean,
    onBackClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoryDetailsViewModel = koinViewModel()
    val state by remember(categoryId) {
        viewModel.loadContent(categoryId)
    }.collectAsState(initial = CategoryDetailsState.Loading)

    CategoryDetailsScreen(
        state = state,
        isSinglePane = isSinglePane,
        onAddTask = { title, dueDate -> viewModel.addTask(title, dueDate, categoryId) },
        onUpdateTaskStatus = { taskId -> viewModel.updateTaskStatus(taskId) },
        onTaskClick = onTaskClick,
        onOptionsClick = {},
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

// ── Screen (Stateless) ────────────────────────────────────────────────────────

@Composable
internal fun CategoryDetailsScreen(
    state: CategoryDetailsState,
    isSinglePane: Boolean,
    onAddTask: (String, LocalDateTime?) -> Unit,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onOptionsClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is CategoryDetailsState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }

        is CategoryDetailsState.Error -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            KuvioLabelMediumText(text = state.throwable.message ?: "Error")
        }

        is CategoryDetailsState.Success -> CategoryDetailsContent(
            category = state.category,
            categoryColor = state.categoryColor,
            groups = state.groups,
            totalTasks = state.totalTasks,
            completedTasks = state.completedTasks,
            onAddTask = onAddTask,
            onUpdateTaskStatus = onUpdateTaskStatus,
            onTaskClick = onTaskClick,
            onOptionsClick = onOptionsClick,
            onBackClick = onBackClick,
            modifier = modifier,
        )
    }
}

// ── Content (Rendering) ───────────────────────────────────────────────────────

@Composable
internal fun CategoryDetailsContent(
    category: Category,
    categoryColor: Color,
    groups: List<TaskGroup>,
    totalTasks: Int,
    completedTasks: Int,
    onAddTask: (String, LocalDateTime?) -> Unit,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onOptionsClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var taskTitle by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var datePickerOpen by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        KuvioCategoryHeader(
            name = category.name,
            color = categoryColor,
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            onOptionsClick = onOptionsClick,
            modifier = Modifier.fillMaxWidth(),
        )

        if (groups.isEmpty()) {
            CategoryDetailsEmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 8.dp),
            ) {
                groups.forEach { group ->
                    item {
                        TaskGroupHeader(
                            group = group,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    items(group.tasks) { task ->
                        KuvioTaskItem(
                            data = task.toItemData(categoryColor, isOverdue = group is TaskGroup.Overdue),
                            onCheckClick = { onUpdateTaskStatus(task.id) },
                            onItemClick = { onTaskClick(task.id) },
                        )
                    }
                }
            }
        }

        KuvioAddTaskBar(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            onAddClick = {
                onAddTask(taskTitle, selectedDate)
                taskTitle = ""
                selectedDate = null
            },
            onDateClick = {
                datePickerOpen = true
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    if (datePickerOpen) {
        DateTimerPicker(
            initialDateTime = selectedDate,
            isDialogOpen = datePickerOpen,
            onCloseDialog = { datePickerOpen = false },
            onDateChange = { date ->
                selectedDate = date
                datePickerOpen = false
            },
        )
    }
}

// ── Private helpers ───────────────────────────────────────────────────────────

@Composable
private fun TaskGroupHeader(group: TaskGroup, modifier: Modifier = Modifier) {
    val label = when (group) {
        is TaskGroup.Overdue -> "Overdue"      // Use stringResource in production
        is TaskGroup.DueToday -> "Due Today"
        is TaskGroup.Upcoming -> "Upcoming"
        is TaskGroup.NoDueDate -> "No Due Date"
        is TaskGroup.Completed -> "Completed"
    }
    KuvioLabelMediumText(
        text = label.uppercase(),
        modifier = modifier,
    )
}

@Composable
private fun CategoryDetailsEmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        KuvioLabelMediumText(text = "No tasks yet")  // Use stringResource
    }
}

private fun Task.toItemData(categoryColor: Color, isOverdue: Boolean = false) = KuvioTaskItemData(
    title = title,
    state = when {
        isCompleted -> KuvioTaskItemState.COMPLETED
        isOverdue -> KuvioTaskItemState.OVERDUE
        else -> KuvioTaskItemState.PENDING
    },
    categoryColor = categoryColor,
)
```

> Note: Replace hardcoded strings (`"Overdue"`, `"No tasks yet"`, etc.) with `stringResource(Res.string.category_details_section_overdue)` calls using the keys added in Task 5. The stubs above are for readability; the real implementation must use string resources.

- [ ] **Build to verify compilation**

```bash
./gradlew :features:category:assemble
```

- [ ] **Commit**

```bash
git add features/category/src/commonMain/kotlin/com/escodro/category/presentation/detail/CategoryDetailsScreen.kt
git commit -m "feat: add CategoryDetailsScreen composables (Section/Screen/Content)"
```

---

## Task 10: Wire navigation in `CategoryNavGraph`

**Files:**
- Modify: `features/category/src/commonMain/kotlin/com/escodro/category/navigation/CategoryNavGraph.kt`

- [ ] **Update the category list entry and add the new `CategoryDetails` entry**

Replace the existing `entry<HomeDestination.CategoryList>` block and add the new `entry<CategoryDestination.CategoryDetails>` block:

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

Add required imports:
```kotlin
import com.escodro.category.presentation.detail.CategoryDetailsSection
import com.escodro.designsystem.config.DesignSystemConfig
import com.escodro.navigationapi.destination.CategoryDestination.CategoryDetails
import com.escodro.navigationapi.event.CategoryEvent
import com.escodro.navigationapi.event.TaskEvent
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.WindowSizeClass
```

- [ ] **Build Android app to verify full wiring**

```bash
./gradlew :app:assembleDebug
```
Expected: Compiles cleanly.

- [ ] **Commit**

```bash
git add features/category/src/commonMain/kotlin/com/escodro/category/navigation/
git commit -m "feat: wire CategoryDetails into NavGraph with feature flag branch"
```

---

## Task 11: UI tests — `CategoryDetailsScreenTest`

**Files:**
- Create: `features/category/src/commonTest/kotlin/com/escodro/category/presentation/detail/CategoryDetailsScreenTest.kt`

Reference: `write-ui-tests` skill. Extends `AlkaaTest()`. Snake_case names. `runComposeUiTest {}`. Wraps in `AlkaaThemePreview`. Strings via `runBlocking { getString(...) }`.

- [ ] **Write UI tests**

```kotlin
package com.escodro.category.presentation.detail

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import com.escodro.resources.Res
import com.escodro.resources.category_details_empty_title
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class CategoryDetailsScreenTest : AlkaaTest() {

    private val testCategory = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt())
    private val testColor = Color(0xFF6200EA)

    @Test
    fun test_emptyStateIsShownWhenNoTasks() = runComposeUiTest {
        // Given
        val emptyTitle = runBlocking { getString(Res.string.category_details_empty_title) }

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = emptyList(),
                    totalTasks = 0,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText(emptyTitle).assertIsDisplayed()
    }

    @Test
    fun test_taskGroupsAreShown() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Buy milk")
        val groups = listOf(TaskGroup.NoDueDate(tasks = listOf(task)))

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = groups,
                    totalTasks = 1,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText("Buy milk").assertIsDisplayed()
    }

    @Test
    fun test_correctSectionHeadersAreDisplayed() = runComposeUiTest {
        // Given
        val task = Task(id = 1L, title = "Task 1")
        val groups = listOf(TaskGroup.NoDueDate(tasks = listOf(task)))
        val sectionHeader = runBlocking {
            getString(Res.string.category_details_section_no_due_date).uppercase()
        }

        // When
        setContent {
            AlkaaThemePreview {
                CategoryDetailsContent(
                    category = testCategory,
                    categoryColor = testColor,
                    groups = groups,
                    totalTasks = 1,
                    completedTasks = 0,
                    onAddTask = { _, _ -> },
                    onUpdateTaskStatus = {},
                    onTaskClick = {},
                    onOptionsClick = {},
                    onBackClick = {},
                )
            }
        }

        // Then
        onNodeWithText(sectionHeader).assertIsDisplayed()
    }
}
```

- [ ] **Run UI tests**

```bash
./gradlew :features:category:desktopTest --tests "*.CategoryDetailsScreenTest"
```
Expected: All pass.

- [ ] **Commit**

```bash
git add features/category/src/commonTest/kotlin/com/escodro/category/presentation/detail/CategoryDetailsScreenTest.kt
git commit -m "test: add CategoryDetailsScreenTest UI tests"
```

---

## Task 12: E2E tests — `CategoryFlowTest` additions

**Files:**
- Modify: `shared/src/commonTest/kotlin/com/escodro/alkaa/CategoryFlowTest.kt`

Reference: `write-e2e-tests` skill. Uses `uiTest {}`. DAO-based seeding. Content descriptions for navigation. `@BeforeTest` / `@AfterTest` with flag reset.

- [ ] **Add E2E tests to `CategoryFlowTest.kt`**

Add these tests to the existing class. Add a `@BeforeTest` cleanup that resets `IsNewDesignEnabled = false` and a `@AfterTest` that also resets it.

Use the existing `addCategory(name)` and `navigateToCategory()` helpers already present in `CategoryFlowTest`. The `addCategory` helper clicks "Add category", types the name, and saves — no direct DAO insertion needed for category seeding.

Add these tests to the existing class, and add flag teardown around each test:

```kotlin
@BeforeTest
fun setUpCategoryDetails() {
    DesignSystemConfig.IsNewDesignEnabled = false  // reset before each test
}

@AfterTest
fun tearDownCategoryDetails() {
    DesignSystemConfig.IsNewDesignEnabled = false  // ensure clean state after each test
}

@Test
fun when_category_is_clicked_and_flag_enabled_then_details_screen_is_shown() = uiTest {
    // Given
    DesignSystemConfig.IsNewDesignEnabled = true
    navigateToCategory()
    addCategory("Work")

    // When
    onNodeWithText("Work").performClick()

    // Then — the header with the category name is displayed (not the bottom sheet)
    onNodeWithText("Work").assertIsDisplayed()
    // Bottom sheet "Save" is absent (would be present if bottom sheet opened instead)
    onNodeWithContentDescription(
        runBlocking { getString(Res.string.task_detail_save_cd) }
    ).assertDoesNotExist()
}

@Test
fun when_category_is_clicked_and_flag_disabled_then_bottom_sheet_is_shown() = uiTest {
    // Given — flag is false (reset in @BeforeTest)
    navigateToCategory()
    addCategory("Work")

    // When
    onNodeWithText("Work").performClick()

    // Then — bottom sheet is present (Save button visible)
    onNodeWithContentDescription(
        runBlocking { getString(Res.string.task_detail_save_cd) }
    ).assertIsDisplayed()
}

@Test
fun when_task_is_added_in_category_details_then_it_appears_in_the_list() = uiTest {
    // Given
    DesignSystemConfig.IsNewDesignEnabled = true
    navigateToCategory()
    addCategory("Work")
    onNodeWithText("Work").performClick()

    // When — type in the AddTaskBar and submit
    val addBarPlaceholder = runBlocking { getString(Res.string.kuvio_add_task_bar_placeholder) }
    onNodeWithText(addBarPlaceholder).performClick()
    onNodeWithText(addBarPlaceholder).performTextInput("Buy groceries")
    onNodeWithContentDescription(
        runBlocking { getString(Res.string.kuvio_add_button_cd) }
    ).performClick()

    // Then
    onNodeWithText("Buy groceries").assertIsDisplayed()
}
```

> Note: Verify the exact content description resource keys (`task_detail_save_cd`, `kuvio_add_button_cd`, `kuvio_add_task_bar_placeholder`) by checking the resources files. If keys differ, update accordingly. Use `runBlocking { getString(Res.string.xyz) }` for all string lookups — never hardcode.

- [ ] **Run E2E tests**

```bash
./gradlew :shared:desktopTest --tests "*.CategoryFlowTest"
```
Expected: New tests PASS (adjust for any content-description mismatches found at runtime).

- [ ] **Commit**

```bash
git add shared/src/commonTest/kotlin/com/escodro/alkaa/CategoryFlowTest.kt
git commit -m "test: add CategoryDetails E2E flow tests"
```

---

## Task 13: Final quality check

- [ ] **Run ktlint format**

```bash
./gradlew ktlintFormat
```

- [ ] **Run detekt**

```bash
./gradlew detektAll
```

- [ ] **Run all tests**

```bash
./gradlew allTests
```
Expected: All pass.

- [ ] **Run full check**

```bash
./gradlew check
```
Expected: No failures.

- [ ] **Commit any ktlint fixes**

```bash
git add -p
git commit -m "style: apply ktlint formatting to CategoryDetails implementation"
```

---

## Acceptance Criteria Checklist

- [ ] AC1: `IsNewDesignEnabled = true` → clicking a category opens the new screen
- [ ] AC2: `IsNewDesignEnabled = false` → clicking a category opens the bottom sheet
- [ ] AC3: Tasks appear in correct sections (Overdue → DueToday → Upcoming → NoDueDate → Completed)
- [ ] AC4: Empty state shown when no tasks exist
- [ ] AC5: Adding a task via AddTaskBar shows it in the list
- [ ] AC6: Adding a task with a due date shows it in the correct section
- [ ] AC7: Completing a task moves it to the Completed section
