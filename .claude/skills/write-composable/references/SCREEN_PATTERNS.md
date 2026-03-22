# Screen Patterns

Every screen in Alkaa follows a strict three-layer pattern:

```
<Feature>Screen        ← public, stateless, called from NavGraph
    └─ <Feature>Loader ← internal, injects ViewModel, collects state
        └─ <Feature>Content ← internal, stateless, renders UI
```

## `<Feature>Screen`

- Public entry point from NavGraph
- Stateless — passes nothing down except navigation callbacks
- Delegates immediately to `<Feature>Loader`

## `<Feature>Loader`

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

## `<Feature>Content` (and variants)

- Internal, stateless — receives a `State` data class, no ViewModel
- Receives all data ready to render (no mapping in composables)
- **This is the composable tested with Compose Testing**
- Add variants (`<Feature>Success`, `<Feature>Error`) when complexity warrants it
