---
name: navigation
description: Use when adding a new screen or modifying navigation in the Alkaa project — triggers on tasks like "add a new screen", "navigate to X", "add destination", "wire up navigation", or "create a navigation event". Also triggers when connecting UI actions to routes in NavGraph.
---

# Alkaa Navigation

## Overview

Alkaa uses event-driven navigation: UI components send named action events, and the system resolves where to go. Navigation is never triggered directly from UI or ViewModels — it always flows through `NavEventController`.

## Required Files per Navigation Change

| File | Package | Purpose |
|------|---------|---------|
| `*Event.kt` | `com.escodro.navigationapi.event` | Defines action events |
| `*Destination.kt` | `com.escodro.navigationapi.destination` | Defines typed routes |
| `*NavGraph.kt` | feature module | Registers entries and sends events |

Both `*Event.kt` and `*Destination.kt` live in `features/navigation-api`. → See `references/CODE_PATTERNS.md` for Kotlin examples of all three files.

## Steps

1. **Create the Event** — Name after the action, not the destination (`OnEditClick`, not `NavigateToEdit`) → see `references/CODE_PATTERNS.md`
2. **Create the Destination** — Add `@Serializable`; choose interface based on screen type → see `references/CODE_PATTERNS.md`
3. **Add a NavGraph Entry** — `entry<Destination>` block with appropriate transition spec → see `references/CODE_PATTERNS.md`
4. **Hoist Navigation** — Composables receive lambdas; NavGraph calls `navEventController.sendEvent()` → see `references/CODE_PATTERNS.md`

## Destination Interface Rules

| Interface | When to use |
|-----------|-------------|
| `TopLevel` | Bottom-nav root screens only (requires `title`, `icon`, `@CommonParcelize`) |
| `TopAppBarVisible` | Dialogs, bottom sheets, non-full-screen destinations |
| Neither | Regular full-screen push destinations |

Also register `TopLevel` and `TopAppBarVisible` destinations in the corresponding sets in `Destination.kt`.

## Checklist

- [ ] Event named after an action (verb phrase: `On*Click`, `On*Save`)
- [ ] Destination annotated with `@Serializable`
- [ ] `TopLevel` only for bottom-nav root screens (with `@CommonParcelize`, `title`, `icon`)
- [ ] `TopAppBarVisible` only for dialogs/sheets/non-full-screen
- [ ] `TopLevel`/`TopAppBarVisible` destinations registered in `Destination.kt` sets
- [ ] NavGraph `entry<>` block exists for each new destination
- [ ] Navigation events sent from within NavGraph lambda, not from composables or ViewModels
- [ ] Back navigation uses `Event.OnBack`, not custom events

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `navEventController.sendEvent()` in a ViewModel | Must not leave the NavGraph — pass navigation as lambda callbacks instead |
| `navEventController.sendEvent()` inside a `@Composable` | Move to NavGraph `entry<>` block via lambda callback |
| Event named `NavigateToDetail` or `GoToSettings` | Name after the action: `OnDetailClick`, `OnSettingsClick` |
| Destination without `@Serializable` | Add annotation — Navigation3 requires it |
| Full-screen destination with `TopAppBarVisible` | Remove — use only for dialogs/sheets |
| New feature with no `NavGraph` implementation | Create `*NavGraph.kt` and bind in Koin module |
