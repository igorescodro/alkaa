---
name: write-e2e-tests
description: Use when writing or modifying end-to-end tests in the Alkaa project — triggers on tasks like "add an E2E test", "write a flow test", "test this feature end-to-end", "add a test to shared module", or "cover this user journey with a test".
---

# Write E2E Tests

## Overview

E2E tests exercise full application flows through a real Koin DI graph and a live `AlkaaMultiplatformApp` Compose tree. They live in `shared/src/commonTest/kotlin/com/escodro/alkaa/` — one file per feature domain (e.g., `TaskFlowTest`, `CategoryFlowTest`).

## Structure

- **Class setup** — Extends `AlkaaTest(), KoinTest`; `@BeforeTest` calls `beforeTest()` first, then cleans DAOs → see `references/SETUP.md`
- **Entry point** — `= uiTest { }` for all tests; `= flakyUiTest { }` only for known timing-sensitive tests → see `references/SETUP.md`
- **Data seeding** — Insert directly via DAOs in `@BeforeTest` using `runTest` → see `references/SETUP.md`
- **Navigation helpers** — Private `ComposeUiTest` extension functions using content descriptions → see `references/SETUP.md`
- **Assertions** — Existence, wait helpers, selection state, semantic matchers → see `references/SETUP.md`
- **Fake dependencies** — `declare<T> { FakeImpl() }` to override external dependencies → see `references/SETUP.md`

## Naming

Descriptive, underscore-separated. Two styles:
- State-driven: `when_no_query_then_all_tasks_are_visible()`
- Action-driven: `add_and_open_task()`, `complete_task_from_list()`

No camelCase, no backtick names.

## Rules

| Rule | Details |
|------|---------|
| **`beforeTest()` first** | Call `beforeTest()` before any DAO injection or `by inject()` use |
| **`cleanTable()` in `@BeforeTest`** | Call on every DAO used to avoid state leakage between tests |
| **DAO-based seeding** | Insert test data directly via DAOs — not via use cases |
| **Content descriptions for navigation** | Navigate via content descriptions, not raw text |
| **Resource strings** | `getString(Res.string.xyz)` inside `uiTest` or `runComposeUiTest` — never hardcode |
| **Fakes over mocks** | Use `declare<T> { FakeImpl() }` for external dependencies |
| **`@AfterTest` always** | Always call `afterTest()` to stop Koin after each test |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Calling `by inject()` before `beforeTest()` | Always call `beforeTest()` first |
| Skipping `cleanTable()` | Call on every DAO to avoid state leakage |
| Using `flakyUiTest` by default | Reserve for tests with known timing sensitivity |
| Hardcoding UI strings | Use `getString(...)` inside `uiTest` or `runComposeUiTest` |
| Testing multiple flows in one test | One scenario per test |
| Missing `@AfterTest` / `afterTest()` | Always stop Koin after each test |
| Navigation via raw text | Navigate via content descriptions |
| Using mocks | Use fakes via `declare<T> { FakeImpl() }` |
