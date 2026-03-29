# E2E Test Setup Reference

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

## Test Entry Point

```kotlin
@Test
fun add_and_complete_task() = uiTest {
    // ...
}
```

- All tests use `= uiTest { }` — sets up the full `AlkaaMultiplatformApp` in a `ComposeUiTest`
- Use `= flakyUiTest { }` **only** for known timing-sensitive tests (e.g. debounce behavior)

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

## Navigation Helpers

Extract navigation steps into private `ComposeUiTest` extension functions:

```kotlin
private fun ComposeUiTest.navigateToCategory() {
    onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()
}
```

Navigate via content descriptions — do not rely on raw text for navigation targets.

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

## Resource Strings

Never hardcode UI strings. Resolve them at test time inside `uiTest` or `runComposeUiTest` (as they are suspendable):

```kotlin
@Test
fun test_something() = uiTest {
    val emptyMessage = getString(Res.string.tracker_header_empty)
    onNodeWithText(text = emptyMessage).assertExists()
}
```

## Fake Dependencies

External dependencies (notifications, alarm schedulers) are already replaced with no-op fakes by `beforeTest()`. For fakes that need different behavior per test class, override with `declare`:

```kotlin
// In @BeforeTest, after calling beforeTest():
declare<CoroutineDebouncer> { CoroutinesDebouncerFake() }
```

Do not use mocks — create lightweight fake implementations.

## Naming Examples

```kotlin
// State-driven
fun when_no_query_then_all_tasks_are_visible()
fun when_tab_changes_then_title_updates()

// Action-driven
fun add_and_open_task()
fun complete_task_from_list()
fun rename_category()
```

## Given / When / Then

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
