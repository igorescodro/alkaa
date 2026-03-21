---
name: navigation
description: Use when adding a new screen or modifying navigation in the Alkaa project — triggers on tasks like "add a new screen", "navigate to X", "add destination", "wire up navigation", or "create a navigation event". Also triggers when connecting UI actions to routes in NavGraph.
---

# Alkaa Navigation

## Overview

Alkaa uses **event-driven navigation**: UI components send named action events, and the system resolves where to go. Navigation is never triggered directly from UI or ViewModels — it always flows through `NavEventController`.

## Required Files per Navigation Change

| File | Package | Purpose |
|------|---------|---------|
| `*Event.kt` | `com.escodro.navigationapi.event` | Defines action events |
| `*Destination.kt` | `com.escodro.navigationapi.destination` | Defines typed routes |
| `*NavGraph.kt` | feature module | Registers entries and sends events |

All files live in `features/navigation-api` (events/destinations) or within the feature module (NavGraph).

## Step-by-Step

### 1. Create the Event

Add to the existing `*Event.kt` object for the feature, or create a new one.

```kotlin
// features/navigation-api/.../event/TaskEvent.kt
object TaskEvent {
    data class OnTaskClick(val id: Long) : Event {
        override fun nextDestination(): Destination = TasksDestination.TaskDetail(taskId = id)
    }

    data object OnNewTaskClick : Event {
        override fun nextDestination(): Destination = TasksDestination.AddTaskBottomSheet
    }
}
```

**Rules:**
- Name after the **action**, not the destination (e.g., `OnEditClick`, not `NavigateToEdit`)
- Use `data object` for parameterless events, `data class` when passing IDs/data
- `nextDestination()` is the only place that maps event → destination

### 2. Create the Destination

Add to the existing `*Destination.kt` object for the feature, or create a new one.

```kotlin
// features/navigation-api/.../destination/TasksDestination.kt
object TasksDestination {
    @Serializable
    data class TaskDetail(val taskId: Long) : Destination          // full screen

    @Serializable
    data object AddTaskBottomSheet : Destination, TopAppBarVisible // dialog/sheet
}
```

**Interface rules:**
- `TopLevel` — only for bottom-nav root screens (requires `title`, `icon`, `@CommonParcelize`)
- `TopAppBarVisible` — for dialogs, bottom sheets, and non-full-screen destinations
- Neither — for regular full-screen push destinations

**`TopLevel` template:**

```kotlin
@Serializable
@CommonParcelize
data object MyFeature : Destination, TopLevel {
    @CommonIgnoredOnParcel
    override val title: StringResource = Res.string.my_title
    @CommonIgnoredOnParcel
    override val icon: ImageVector = Icons.Outlined.MyIcon
}
```

Also register in `TopLevelDestinations` and `TopAppBarVisibleDestinations` in `Destination.kt`.

### 3. Add a NavGraph Entry

> If the screen composable doesn't exist yet, use the `write-composable` skill to create it first.
> If the `TopLevel` destination title is a new string, use the `localization` skill to add it.

In the feature's `*NavGraph.kt`, add an `entry<>` block for each new destination:

```kotlin
// features/task/.../navigation/TaskNavGraph.kt
internal class TaskNavGraph : NavGraph {
    override val navGraph: EntryProviderScope<Destination>.(NavEventController) -> Unit =
        { navEventController ->
            entry<TasksDestination.TaskDetail>(
                metadata = NavDisplay.transitionSpec { SlideInHorizontallyTransition } +
                    NavDisplay.popTransitionSpec { SlideOutHorizontallyTransition } +
                    NavDisplay.predictivePopTransitionSpec { SlideOutHorizontallyTransition },
            ) { entry ->
                TaskDetailScreen(
                    taskId = entry.taskId,
                    onUpPress = { navEventController.sendEvent(Event.OnBack) },
                )
            }
        }
}
```

**Transition conventions:**
- Full-screen push/detail: `SlideInHorizontally` / `SlideOutHorizontally`
- Top-level tabs: `FadeIn` / `FadeOut`
- Dialogs/bottom sheets: `DialogSceneStrategy.dialog()` metadata

### 4. Hoist Navigation in the UI

UI composables receive callbacks as lambdas. They call the callback; the screen/section calls `navEventController.sendEvent()`:

```kotlin
// In NavGraph entry (correct — event is sent here)
entry<HomeDestination.TaskList> {
    TaskListSection(
        onItemClick = { taskId ->
            navEventController.sendEvent(TaskEvent.OnTaskClick(taskId))
        },
        onFabClick = {
            navEventController.sendEvent(TaskEvent.OnNewTaskClick)
        },
    )
}

// TaskListSection signature (correct — receives lambdas, doesn't know about navigation)
@Composable
fun TaskListSection(
    onItemClick: (Long) -> Unit,
    onFabClick: () -> Unit,
)
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `navEventController.sendEvent()` in a ViewModel | `NavEventController` must not leave the NavGraph — pass navigation as lambda callbacks instead |
| `navEventController.sendEvent()` directly inside a `@Composable` | Move the send to the NavGraph `entry<>` block via a lambda callback |
| Event named `NavigateToDetail` or `GoToSettings` | Name after the action, not the destination: `OnDetailClick`, `OnSettingsClick` |
| Destination without `@Serializable` | Add `@Serializable` annotation — Navigation3 requires it |
| Full-screen destination implementing `TopAppBarVisible` without `TopLevel` | Remove `TopAppBarVisible`; use it only for dialogs, sheets, and non-full-screen destinations |
| New feature with no `NavGraph` implementation | Create `*NavGraph.kt` and bind in the Koin module |

## Checklist

- [ ] Event named after an action (verb phrase: `On*Click`, `On*Save`)
- [ ] Destination annotated with `@Serializable`
- [ ] `TopLevel` only for bottom-nav root screens (with `@CommonParcelize`, `title`, `icon`)
- [ ] `TopAppBarVisible` only for dialogs/sheets/non-full-screen
- [ ] `TopLevel`/`TopAppBarVisible` destinations registered in `Destination.kt` sets
- [ ] NavGraph `entry<>` block exists for each new destination
- [ ] Navigation events sent from within NavGraph lambda, not from composables or ViewModels
- [ ] Back navigation uses `Event.OnBack`, not custom events
