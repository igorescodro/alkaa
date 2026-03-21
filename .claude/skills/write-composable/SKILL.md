---
name: write-composable
description: Use when creating a new Composable or modifying an existing one in the Alkaa project — screen structure, state handling, adaptive layouts, Kuvio usage, or previews.
---

# Write Composable

## Overview

Composables in Alkaa follow a strict three-layer Screen → Loader → Content pattern. All UI uses Kuvio components exclusively, state flows from ViewModel through the Loader, and every composable requires both dark and light previews.

## Screen Structure

Every screen follows the three-layer pattern:

```
<Feature>Screen        ← public, stateless, called from NavGraph
    └─ <Feature>Loader ← internal, injects ViewModel, collects state
        └─ <Feature>Content ← internal, stateless, renders UI
```

### `<Feature>Screen`
- Public entry point from NavGraph
- Stateless — passes nothing down except navigation callbacks
- Delegates immediately to `<Feature>Loader`

### `<Feature>Loader`
- Internal (`internal`)
- Injects ViewModel via `koinViewModel()`
- Collects **all** ViewModel state with `remember + collectAsState()`
- No UI rendering — delegates to `<Feature>Content`

```kotlin
@Composable
internal fun TaskLoader(onNavigateBack: () -> Unit) {
    val viewModel = koinViewModel<TaskViewModel>()
    val state by remember(viewModel) { viewModel.state }.collectAsState()
    TaskContent(state = state, onNavigateBack = onNavigateBack)
}
```

### `<Feature>Content` (and variants)
- Internal, stateless — receives a `State` data class, no ViewModel
- Receives all data ready to render (no mapping in composables)
- **This is the composable tested with Compose Testing**
- Add variants (`<Feature>Success`, `<Feature>Error`) when complexity warrants it

## Overall Rules

| Rule | Details |
|------|---------|
| **Kuvio only** | Use `KuvioText`, `KuvioIcon`, etc. — never raw `Text`, `Icon`, or Material components directly. If a base component is missing from Kuvio, implement it first. |
| **Modifier param** | Every rendering composable accepts an optional `modifier: Modifier = Modifier` |
| **Paddings** | Always even numbers, ideally multiples of 4 |
| **Snackbar** | Use Snackbar, never Toast |
| **Adaptive** | Screens must work on landscape, tablets, and desktop using Material Adaptive components |
| **Previews** | All composables need both dark and light previews. Use a single interactive preview with state over multiple static ones. |

## Adaptive Layouts

Screens with a list→detail relationship must support two-pane layouts on wide windows (tablets, desktop, landscape). The `isSinglePane` boolean drives all branching — it originates at the NavGraph and flows down unchanged through every layer.

### Where isSinglePane comes from

Always computed at the **NavGraph entry** using the extension from `navigation-api`:

```kotlin
// In NavGraph entry block
TaskListSection(
    isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane(),
)
```

Returns `true` below 600dp (phone portrait), `false` on wider windows. Nested detail destinations (pushed via back-stack) always receive `isSinglePane = true` — they are always full-screen.

### Parameter flow

`isSinglePane` passes top-to-bottom without modification:

```
NavGraph entry    → computes isSinglePane
  └─ <Feature>Section(isSinglePane)
      └─ <Feature>Loader(isSinglePane)   ← branches here
          ├─ (true)  <Feature>Scaffold              ← standard layout
          └─ (false) Adaptive<Feature>Scaffold      ← ListDetailPaneScaffold
```

### Loader branching

The **Loader** is where the single/two-pane decision lives, not the Content:

```kotlin
@Composable
internal fun TaskListLoader(
    isSinglePane: Boolean,
    onItemClick: (Long) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = koinInject(),
) {
    val state by remember(viewModel) { viewModel.loadTaskList() }
        .collectAsState(TaskListViewState.Loading)

    if (isSinglePane) {
        TaskListScaffold(
            state = state,
            onItemClick = onItemClick,   // navigates via NavEventController
            onFabClick = onFabClick,
            modifier = modifier,
        )
    } else {
        AdaptiveTaskListScaffold(
            state = state,
            onFabClick = onFabClick,
            modifier = modifier,
        )
    }
}
```

### Two-pane scaffold

Use `ListDetailPaneScaffold` + `ThreePaneScaffoldNavigator` for the selected-item state:

```kotlin
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AdaptiveTaskListScaffold(
    state: TaskListViewState,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<TaskId>()
    val coroutineScope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                TaskListScaffold(
                    state = state,
                    onItemClick = { taskId ->
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, TaskId(taskId))
                        }
                    },
                    onFabClick = onFabClick,
                    modifier = modifier,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val taskId = navigator.currentDestination?.contentKey?.value
                if (taskId != null) {
                    TaskDetailScreen(
                        isSinglePane = false,   // side-panel mode
                        taskId = taskId,
                        onUpPress = { coroutineScope.launch { navigator.navigateBack() } },
                    )
                } else {
                    DefaultIconTextContent(   // empty-state placeholder
                        icon = Icons.Outlined.CheckCircle,
                        header = stringResource(Res.string.task_detail_pane_title),
                    )
                }
            }
        },
    )
}
```

**Rules:**
- `ThreePaneScaffoldNavigator<T>` where `T` is the selected item's ID type
- `navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)` — always call inside a `coroutineScope.launch`
- `navigator.currentDestination?.contentKey` is nullable — always handle the empty state
- Detail pane always passes `isSinglePane = false`
- `@OptIn(ExperimentalMaterial3AdaptiveApi::class)` required on the composable

### Toolbar adapts to isSinglePane

Detail screens pass `isSinglePane` to `AlkaaToolbar`, which changes the navigation icon:

| `isSinglePane` | Icon | Meaning |
|----------------|------|---------|
| `true` | Back arrow | Full-screen push navigation |
| `false` | Close (×) | Side panel, dismiss in place |

### When NOT to use ListDetailPaneScaffold

Only screens with a canonical list→detail drill-down need two-pane layouts (Task, Search). Screens without a detail (Category edit dialogs, Preference sub-screens) use standard `Scaffold` and remain single-pane; use `BoxWithConstraints` for responsive grid sizing if needed.

Reference file: `features/task/src/commonMain/kotlin/com/escodro/task/presentation/list/TaskList.kt`

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Injecting ViewModel or dependencies outside a `Loader` | Move all `koinViewModel()` / `koinInject()` calls to `<Feature>Loader` |
| Using raw `Text(style = ...)` | Use the appropriate `Kuvio*Text` variant instead |
| Mapping or transforming data inside a composable | Push logic to the ViewModel; composables only render |
| Missing `Modifier` parameter on a rendering composable | Add `modifier: Modifier = Modifier` to every rendering function |
| Single preview without dark/light variants | Always provide both light and dark `@Preview` |
| Odd or non-multiple-of-4 padding values | Use multiples of 4 (4, 8, 12, 16, 24 dp) |
| Computing `isSinglePane` inside a composable | Always compute at the NavGraph entry, never inside the screen |
| Passing `isSinglePane = true` to the detail pane inside `ListDetailPaneScaffold` | The side-panel detail always receives `false` |
| Calling `navigator.navigateTo()` without `coroutineScope.launch` | `navigateTo` is a suspend function — wrap in `coroutineScope.launch` |

## Related Skills

- **Missing Kuvio component** → use `write-design-system-component` skill before implementing the composable
- **User-facing strings** (labels, button text, content descriptions) → use `localization` skill
- **Testing composable behavior** → use `write-ui-tests` skill

## Red Flags

Stop if you notice:
- `koinInject()` or `get()` called outside of a `Loader`
- `Text(text = ..., style = ...)` anywhere in the code
- No `Modifier` parameter on a rendering composable
- A single preview without dark/light variants
- Odd padding or padding not a multiple of 4
- A needed Kuvio component doesn't exist → implement it with `write-design-system-component` first
