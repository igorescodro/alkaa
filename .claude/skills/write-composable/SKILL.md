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

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Injecting ViewModel or dependencies outside a `Loader` | Move all `koinViewModel()` / `koinInject()` calls to `<Feature>Loader` |
| Using raw `Text(style = ...)` | Use the appropriate `Kuvio*Text` variant instead |
| Mapping or transforming data inside a composable | Push logic to the ViewModel; composables only render |
| Missing `Modifier` parameter on a rendering composable | Add `modifier: Modifier = Modifier` to every rendering function |
| Single preview without dark/light variants | Always provide both light and dark `@Preview` |
| Odd or non-multiple-of-4 padding values | Use multiples of 4 (4, 8, 12, 16, 24 dp) |

## Red Flags

Stop if you notice:
- `koinInject()` or `get()` called outside of a `Loader`
- `Text(text = ..., style = ...)` anywhere in the code
- No `Modifier` parameter on a rendering composable
- A single preview without dark/light variants
- Odd padding or padding not a multiple of 4
