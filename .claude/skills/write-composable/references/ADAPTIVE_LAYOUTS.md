# Adaptive Layouts

Screens with a list→detail relationship must support two-pane layouts on wide windows (tablets, desktop, landscape). The `isSinglePane` boolean drives all branching — it originates at the NavGraph and flows down unchanged through every layer.

## Where isSinglePane comes from

Always computed at the **NavGraph entry** using the extension from `navigation-api`:

```kotlin
// In NavGraph entry block
TaskListSection(
    isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane(),
)
```

Returns `true` below 600dp (phone portrait), `false` on wider windows. Nested detail destinations (pushed via back-stack) always receive `isSinglePane = true` — they are always full-screen.

## Parameter flow

`isSinglePane` passes top-to-bottom without modification:

```
NavGraph entry    → computes isSinglePane
  └─ <Feature>Section(isSinglePane)
      └─ <Feature>Loader(isSinglePane)   ← branches here
          ├─ (true)  <Feature>Scaffold              ← standard layout
          └─ (false) Adaptive<Feature>Scaffold      ← ListDetailPaneScaffold
```

## Loader branching

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

## Two-pane scaffold

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

## Toolbar adapts to isSinglePane

Detail screens pass `isSinglePane` to `AlkaaToolbar`, which changes the navigation icon:

| `isSinglePane` | Icon | Meaning |
|----------------|------|---------|
| `true` | Back arrow | Full-screen push navigation |
| `false` | Close (×) | Side panel, dismiss in place |

## When NOT to use ListDetailPaneScaffold

Only screens with a canonical list→detail drill-down need two-pane layouts (Task, Search). Screens without a detail (Category edit dialogs, Preference sub-screens) use standard `Scaffold` and remain single-pane; use `BoxWithConstraints` for responsive grid sizing if needed.

Reference file: `features/task/src/commonMain/kotlin/com/escodro/task/presentation/list/TaskList.kt`
