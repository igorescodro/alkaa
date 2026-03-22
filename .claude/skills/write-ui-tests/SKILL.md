---
name: write-ui-tests
description: Use when writing or modifying UI/Compose instrumented tests in the Alkaa project — triggers on tasks like "add a UI test", "test this composable", "add instrumented test", "test this screen behavior".
---

# Write UI Tests

## Overview

UI tests in Alkaa test Compose and UI behavior in isolation — not integration tests. Composables must be stateless to make testing straightforward: pass all state and callbacks as parameters, then assert on the semantic tree.

Tests live in: `features/<feature>/src/commonTest/kotlin/com/escodro/<feature>/presentation/instrumented/`

## Structure

- **Class setup** — Extends `AlkaaTest()`, `@OptIn(ExperimentalTestApi::class)` on class → see `references/SETUP.md`
- **Test body** — `= runComposeUiTest { }` for each test
- **Loading composables** — Private extension function on `ComposeUiTest`; always wrap with `AlkaaThemePreview` → see `references/SETUP.md`
- **Fakes** — Implement interface, register in Koin test module, clean in `@AfterTest` → see `references/SETUP.md`
- **Assertions** — By text, content description, or test tag; never by component ID → see `references/SETUP.md`

## Naming

Snake_case, two accepted forms:
- Standard: `test_emptyViewIsShown()`
- State-driven: `when_view_is_opened_then_empty_view_is_shown()`

No camelCase, no backtick names.

## Rules

| Rule | Details |
|------|---------|
| **Always wrap** | Wrap composable in `AlkaaThemePreview` |
| **Resource strings** | `runBlocking { getString(Res.string.xyz) }` — never hardcode |
| **One scenario per test** | Split different states into separate `@Test` functions |
| **`useUnmergedTree = true`** | When nodes are inside merged semantics |
| **`@IgnoreOnDesktop`** | For tests not applicable on desktop |
| **`@AfterTest` cleanup** | Call `clean()` on every fake |
| **Given/When/Then** | Always add the three comment blocks |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Testing a stateful composable directly | Extract a stateless Content composable and test that |
| Using mocks for domain interfaces | Create a fake implementing the interface |
| Hardcoding UI strings | Use `runBlocking { getString(...) }` |
| Testing multiple scenarios in one test | Split into separate `@Test` functions |
| Missing `@AfterTest` cleanup | Add `tearDown()` calling `clean()` on every fake |
| Forgetting `AlkaaThemePreview` wrapper | Always wrap composable |
| Omitting Given/When/Then comments | Always add the three comment blocks |
