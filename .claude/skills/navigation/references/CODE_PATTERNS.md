# Navigation Code Patterns

## Step 1. Create the Event

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

## Step 2. Create the Destination

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

| Interface | When to use |
|-----------|-------------|
| `TopLevel` | Bottom-nav root screens only (requires `title`, `icon`, `@CommonParcelize`) |
| `TopAppBarVisible` | Dialogs, bottom sheets, non-full-screen destinations |
| Neither | Regular full-screen push destinations |

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

## Step 3. Add a NavGraph Entry

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

| Screen type | Transition |
|-------------|------------|
| Full-screen push/detail | `SlideInHorizontally` / `SlideOutHorizontally` |
| Top-level tabs | `FadeIn` / `FadeOut` |
| Dialogs/bottom sheets | `DialogSceneStrategy.dialog()` metadata |

## Step 4. Hoist Navigation in the UI

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

Composables receive callbacks as lambdas. They call the callback; the NavGraph `entry<>` block calls `navEventController.sendEvent()`.
