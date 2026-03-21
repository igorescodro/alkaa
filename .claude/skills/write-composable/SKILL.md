---
name: write-composable
description: Use when creating a new Composable or modifying an existing one in the Alkaa project — screen structure, state handling, adaptive layouts, Kuvio usage, or previews.
---

# Alkaa Composable Conventions

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

## What NOT to Do

- **Inject** classes (ViewModel, use cases, repositories) in composable bodies or outside of `Loader` parameters
- **Use** `Text(style = ...)` — use the appropriate `Kuvio*Text` variant instead
- **Map or transform** data in composables — push logic to ViewModel
- **Implement** complex logic inside composables

## Red Flags

Stop if you notice:
- A composable calling `koinInject()` or `get()` outside of a `Loader`
- `Text(text = ..., style = ...)` anywhere in the codebase being modified
- No `Modifier` parameter on a rendering composable
- A single preview without dark/light variants
- Hardcoded padding values that are odd or aren't multiples of 4
