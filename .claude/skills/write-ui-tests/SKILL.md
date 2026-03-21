---
name: write-ui-tests
description: Use when writing or modifying UI/Compose instrumented tests in the Alkaa project — triggers on tasks like "add a UI test", "test this composable", "add instrumented test", "test this screen behavior".
---

# Write UI Tests

## Overview

UI tests in Alkaa test **Compose and UI behavior** in isolation — they are not integration tests. Composables should be as stateless as possible to make testing straightforward: pass all state and callbacks as parameters, then assert on the semantic tree.

## File Location

Tests live in `commonTest` inside an `instrumented` package within each feature module:

```
features/<feature>/src/commonTest/kotlin/com/escodro/<feature>/presentation/instrumented/
```

## Test Class Setup

```kotlin
@OptIn(ExperimentalTestApi::class)
internal class TaskListTest : AlkaaTest() {

    @Test
    fun test_emptyViewIsShown() = runComposeUiTest {
        // ...
    }
}
```

- Extend `AlkaaTest()` — handles Robolectric on Android, no-op on Desktop
- `@OptIn(ExperimentalTestApi::class)` on the class
- `internal` visibility
- Each test body: `= runComposeUiTest { }`

## Naming

Use snake_case with two accepted forms:

```kotlin
// Standard
fun test_emptyViewIsShown()
fun test_dueDateIsShown()
fun test_allAlarmIntervalsCanBeSelected()

// State-driven: when_<precondition>_then_<expected>
fun when_view_is_opened_then_empty_view_is_shown()
fun when_view_has_items_then_items_are_shown()
```

**No camelCase, no backtick names** in UI tests.

## Given / When / Then

Use comments to separate the three blocks:

```kotlin
@Test
fun test_errorViewIsShown() = runComposeUiTest {
    // Given an error state
    val state = TaskListViewState.Error(IllegalStateException())

    // When the view is loaded
    loadTaskList(state)

    // Then the error view is shown
    val header = runBlocking { getString(Res.string.task_list_header_error) }
    onNodeWithText(text = header).assertExists()
}
```

## Loading Composables

Define a private extension function on `ComposeUiTest` to encapsulate composable setup:

```kotlin
// Stateless — no DI needed
private fun ComposeUiTest.loadTaskList(state: TaskListViewState) {
    setContent {
        AlkaaThemePreview {
            TaskListScaffold(
                taskViewState = state,
                onFabClick = {},
                onItemClick = {},
                modifier = Modifier,
            )
        }
    }
}

// With DI — wrap with KoinApplication
private fun ComposeUiTest.loadTaskDetail(state: TaskDetailState) = setContent {
    KoinApplication(application = { modules(testModule) }) {
        AlkaaThemePreview {
            TaskDetailRouter(
                detailViewState = state,
                actions = TaskDetailActions(),
            )
        }
    }
}
```

- Always wrap with `AlkaaThemePreview`
- Pass all callbacks as `{}`
- Only use `KoinApplication` when the composable requires injected dependencies

## Fakes Over Mocks

Prefer fakes. Only use mocks for types you cannot create an interface for (Android/Framework types like `Context`).

```kotlin
class PermissionControllerFake : PermissionController {
    var isPermissionGrantedValue: Boolean = true

    override suspend fun isPermissionGranted(permission: Permission) =
        isPermissionGrantedValue

    override suspend fun requestPermission(permission: Permission) = Unit

    fun clean() {
        isPermissionGrantedValue = true
    }
}
```

Register in a Koin test module and reset in `@AfterTest`:

```kotlin
private val permissionsController = PermissionControllerFake()

private val testModule = module {
    single<PermissionController> { permissionsController }
}

@AfterTest
fun tearDown() {
    permissionsController.clean()
}
```

## Semantic Assertions

Query the UI by text, content description, or test tag — never by component IDs:

```kotlin
// Existence
onNodeWithText(text = header).assertExists()
onNodeWithContentDescription(label = contentDescription).assertExists()

// Visibility
onNodeWithText(noAlarmString).assertIsDisplayed()

// Non-existence
onNodeWithText(repeatIconCd).assertDoesNotExist()

// Selection state
onChip(category.name).assertIsSelected()
onChip(other.name).assertIsNotSelected()

// Interactions
onNodeWithText(label).performClick()
onNodeWithContentDescription(cd, useUnmergedTree = true).performClick()

// Multiple matches
onAllNodesWithText(intervalString)[0].performClick()
```

Use `useUnmergedTree = true` when nodes are nested inside merged semantics.

## Resource Strings

Resolve Compose Multiplatform string resources at test time via `runBlocking`:

```kotlin
val header = runBlocking { getString(Res.string.task_list_header_error) }
onNodeWithText(text = header).assertExists()
```

Never hardcode UI strings in tests.

## Platform Annotations

Skip tests that are not applicable on a specific platform:

```kotlin
@Test
@IgnoreOnDesktop
fun test_permissionDialogIsShown() = runComposeUiTest { /* ... */ }
```

## One Scenario Per Test

Each test covers exactly one behavior:

```kotlin
// ✅ Good — separate tests
fun test_errorViewIsShown()
fun test_emptyViewIsShown()
fun test_listViewIsShown()

// ❌ Bad — multiple assertions covering different states
fun test_allViewStates()
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Testing a stateful composable directly (injected ViewModel) | Extract a stateless Content composable and test that instead |
| Using mocks for domain interfaces | Create a fake implementing the interface |
| Hardcoding UI strings | Use `runBlocking { getString(Res.string.xyz) }` |
| Testing multiple scenarios in one test | Split into separate `@Test` functions |
| Missing `@AfterTest` cleanup | Add `tearDown()` calling `clean()` on every fake |
| Forgetting `AlkaaThemePreview` wrapper | Always wrap composable in `AlkaaThemePreview` |
| Omitting Given/When/Then comments | Always add the three comment blocks |
