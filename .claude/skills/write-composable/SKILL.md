---
name: write-composable
description: Use when creating a new Composable or modifying an existing one in the Alkaa project — screen structure, state handling, adaptive layouts, Kuvio usage, or previews.
---

# Write Composable

## Overview

Composables in Alkaa follow a strict three-layer Screen → Loader → Content pattern. All UI uses Kuvio components exclusively, state flows from ViewModel through the Loader, and every composable requires both dark and light previews.

## Screen Structure

Every screen has three layers: `<Feature>Screen` (public, stateless, NavGraph entry point) → `<Feature>Loader` (internal, injects ViewModel, collects state) → `<Feature>Content` (internal, stateless, tested with Compose Testing).

→ See `references/SCREEN_PATTERNS.md` for Kotlin code examples of each layer.

For screens with list→detail relationships, a two-pane adaptive layout is required on wide windows (tablets, desktop). The `isSinglePane` boolean originates at the NavGraph entry and flows unchanged to the Loader where branching happens.

→ See `references/ADAPTIVE_LAYOUTS.md` for `isSinglePane` flow, Loader branching pattern, `ListDetailPaneScaffold`, and toolbar adaptation.

## Rules

| Rule | Details |
|------|---------|
| **Kuvio only** | Use `KuvioText`, `KuvioIcon`, etc. — never raw `Text`, `Icon`, or Material components |
| **Modifier param** | Every rendering composable accepts `modifier: Modifier = Modifier` |
| **Paddings** | Always even numbers, multiples of 4 (4, 8, 12, 16, 24 dp) |
| **Snackbar** | Use Snackbar, never Toast |
| **Adaptive** | Screens must work on landscape, tablets, and desktop |
| **Previews** | All composables need both dark and light previews |
| **isSinglePane origin** | Always computed at NavGraph entry via `currentWindowAdaptiveInfo().windowSizeClass.isSinglePane()` |

## Red Flags

Stop if you notice:
- `koinInject()` or `get()` called outside of a `Loader`
- `Text(text = ..., style = ...)` anywhere in the code
- No `Modifier` parameter on a rendering composable
- A single preview without dark/light variants
- Odd padding or padding not a multiple of 4
- A needed Kuvio component doesn't exist → implement with `write-design-system-component` first

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Injecting ViewModel outside a Loader | Move all `koinViewModel()` / `koinInject()` calls to `<Feature>Loader` |
| Using raw `Text(style = ...)` | Use the appropriate `Kuvio*Text` variant instead |
| Mapping or transforming data inside a composable | Push logic to the ViewModel; composables only render |
| Missing `Modifier` parameter on a rendering composable | Add `modifier: Modifier = Modifier` to every rendering function |
| Single preview without dark/light variants | Always provide both light and dark `@Preview` |
| Non-multiple-of-4 padding | Use multiples of 4 (4, 8, 12, 16, 24 dp) |
| Computing `isSinglePane` inside a composable | Always compute at NavGraph entry, never inside the screen |
| Passing `isSinglePane = true` to detail pane in `ListDetailPaneScaffold` | Side-panel detail always receives `false` |
| Calling `navigator.navigateTo()` without `coroutineScope.launch` | `navigateTo` is a suspend function — wrap in `coroutineScope.launch` |

## Related Skills

- **Missing Kuvio component** → use `write-design-system-component` skill before implementing the composable
- **User-facing strings** → use `localization` skill
- **Testing composable behavior** → use `write-ui-tests` skill
