---
name: write-e2e-tests
description: Use when writing or modifying end-to-end tests in the Alkaa project — triggers on tasks like "add an E2E test", "write a flow test", "test this feature end-to-end", "add a test to shared module", or "cover this user journey with a test".
---

# Write E2E Tests

## Overview

E2E tests in Alkaa exercise full application flows through a real Koin DI graph and a live `AlkaaMultiplatformApp` Compose tree. They live in the `shared` module's `commonTest` source set and are **not** isolated unit or component tests — they simulate real user interaction from navigation to data persistence.

---

## File Location

All E2E tests live in:

```
shared/src/commonTest/kotlin/com/escodro/alkaa/
```

Each file covers one feature domain (e.g. `TaskFlowTest`, `CategoryFlowTest`, `SearchFlowTest`).

---

## Test Class Setup

```kotlin
@OptIn(ExperimentalTestApi::class)
internal class MyFeatureFlowTest : AlkaaTest(), KoinTest {

    private val taskDao: TaskDao by inject()

    @BeforeTest
    fun setup() {
        beforeTest()
        runTest {
            taskDao.cleanTable()
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }
}
```

- `internal` visibility
- Extends `AlkaaTest(), KoinTest`
- `@OptIn(ExperimentalTestApi::class)` on the class
- `@BeforeTest` must call `beforeTest()` **first**, then clean DAOs via `runTest`
- `@AfterTest` must always call `afterTest()`
- DAOs injected with `by inject()` — only available after `beforeTest()` starts Koin

---

## Test Entry Point

```kotlin
@Test
fun add_and_complete_task() = uiTest {
    // ...
}
```

- All tests use `= uiTest { }` — sets up the full `AlkaaMultiplatformApp` in a `ComposeUiTest`
- Use `= flakyUiTest { }` **only** for known timing-sensitive tests (e.g. debounce behavior)

---

## Naming

Descriptive, underscore-separated. Two accepted styles:

```kotlin
// State-driven
fun when_no_query_then_all_tasks_are_visible()
fun when_tab_changes_then_title_updates()

// Action-driven
fun add_and_open_task()
fun complete_task_from_list()
fun rename_category()
```

No camelCase, no backtick names.

---

## Given / When / Then

Use comments to separate the three blocks:

```kotlin
@Test
fun edit_task_name() = uiTest {
    // Given an open task with a typo in the title
    addAndOpenTask("Watter planttes")

    // When the name is corrected
    onAllNodes(hasSetTextAction())[0].performTextReplacement("Water plants")
    navigateBack()

    // Then the corrected name is visible in the list
    waitUntilAtLeastOneExists(hasText("Water plants"))
    onNodeWithText(text = "Water plants", useUnmergedTree = true).assertExists()
}
```

---

## Pre-Seeding Test Data

Insert data directly via DAOs in `@BeforeTest` using `runTest`:

```kotlin
runTest {
    categoryDao.insertCategory(
        Category(category_id = 11, category_name = "Books", category_color = "#cc5a71"),
    )
}
```

For reusable data sets, define them as `val FAKE_*` constants in a `fake/` companion file.

---

## Navigation Helpers

Extract navigation steps into private `ComposeUiTest` extension functions:

```kotlin
private fun ComposeUiTest.navigateToCategory() {
    onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()
}
```

Navigate via content descriptions — do not rely on raw text for navigation targets.

---

## Assertions

```kotlin
// Existence
onNodeWithText("No alarm").assertExists()
onNodeWithContentDescription("Description").assertExists()

// Non-existence
waitUntilDoesNotExist(hasText(taskName))
onNodeWithText("No alarm").assertDoesNotExist()

// Wait helpers
waitUntilAtLeastOneExists(hasText(taskName))
waitUntilExactlyOneExists(hasSetTextAction())

// Selection state
onAllNodes(hasText(category)).onLast().assertIsSelected()

// Custom semantic matchers
onNode(SemanticsMatcher.expectValue(CheckboxNameKey, taskName)).performClick()
onNode(SemanticsMatcher.expectValue(ColorKey, color)).assertExists()
```

Use `useUnmergedTree = true` when nodes are inside merged semantics.

---

## Resource Strings

Never hardcode UI strings. Resolve them at test time:

```kotlin
val emptyMessage = runBlocking { getString(Res.string.tracker_header_empty) }
onNodeWithText(text = emptyMessage).assertExists()
```

---

## Fake Dependencies

External dependencies (notifications, alarm schedulers) are already replaced with no-op fakes by `beforeTest()`. For fakes that need different behavior per test class, override with `declare`:

```kotlin
// In @BeforeTest, after calling beforeTest():
declare<CoroutineDebouncer> { CoroutinesDebouncerFake() }
```

Do not use mocks — create lightweight fake implementations.

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Calling `by inject()` before `beforeTest()` | Always call `beforeTest()` first in `@BeforeTest` |
| Skipping `cleanTable()` in `@BeforeTest` | Call `cleanTable()` on every DAO used to avoid state leakage |
| Using `flakyUiTest` by default | Reserve it for tests with known timing sensitivity (debounce, etc.) |
| Hardcoding UI strings | Use `runBlocking { getString(...) }` |
| Testing multiple flows in one test | One scenario per test — split into separate `@Test` functions |
| Missing `@AfterTest` / `afterTest()` | Always stop Koin after each test |
| Forgetting Given/When/Then comments | Add all three comment blocks even for simple tests |
| Using mocks | Use fakes declared via `declare<T> { FakeImpl() }` |
| Navigation via raw text | Navigate via content descriptions |
