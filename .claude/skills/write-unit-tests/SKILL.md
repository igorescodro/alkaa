---
name: write-unit-tests
description: Use when writing or modifying unit tests in the Alkaa project — triggers on tasks like "add a test", "write tests for X", "test this ViewModel", "cover this use case with tests", or "add unit test coverage".
---

# Write Unit Tests

## Overview

Unit tests in Alkaa are multiplatform-first, fake-based, and structured around a single scenario per test. Tests live in `commonTest` and run fastest via `./gradlew desktopTest`.

## Structure

- **Given / When / Then** — Use comments to separate the three blocks in every test → see `references/TEST_EXAMPLES.md`
- **Backtick names** — Start with `test if` or `check if`, describe the specific condition and expected outcome
- **One scenario per test** — Split side effects, error paths, and edge cases into separate tests
- **Fakes only** — Never mocks for domain interfaces → see `references/FAKE_PATTERNS.md` for all three fake patterns
- **CoroutinesTestDispatcher** — Delegate `by CoroutinesTestDispatcherImpl()` for any coroutine-using test class → see `references/TEST_EXAMPLES.md`
- **ViewModel test setup** → see `references/TEST_EXAMPLES.md`
- **Use case / repository test setup** → see `references/TEST_EXAMPLES.md`

## Naming

```
✅ `test if when load tasks fails the error state is returned`
✅ `test if task is updated as completed`
✅ `test if category without name is not added`
❌ testUpdateTask()
❌ test1()
```

## Rules

| Rule | Details |
|------|---------|
| **Fakes over mocks** | Only use mocks for Android/Framework types you cannot interface (e.g., `Context`) |
| **CoroutinesTestDispatcher** | Delegate `by CoroutinesTestDispatcherImpl()` for any coroutine-using test class |
| **ViewModel at field level** | Construct ViewModel at field level, not inside `@BeforeTest` |
| **`runTest` for suspend** | Use `= runTest { }` for all suspend test bodies |
| **`flow.first()`** | Collect current state with `flow.first()` |
| **`require()` for narrowing** | Use `require(state is X)` before accessing state-specific properties |
| **`@BeforeTest` cleanup** | Call `clean()` on every fake to reset state between tests |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Using mocks for domain interfaces | Create a fake implementing the interface |
| Forgetting `CoroutinesTestDispatcher` delegation | Add `by CoroutinesTestDispatcherImpl()` |
| Testing multiple scenarios in one test | Split into separate `@Test` functions |
| Skipping `@BeforeTest` cleanup | Add `setup()` calling `clean()` on every fake |
| Constructing ViewModel in `@BeforeTest` | Construct at field level so it shares dispatcher context |
| Omitting Given/When/Then comments | Always add the three comment blocks |
| Vague test names like `testUpdate()` | Rename to describe the scenario and expected outcome |

## Related Skills

- **ViewModel tests** → use `write-viewmodel` skill for ViewModel construction patterns
- **UI behavior tests** → use `write-ui-tests` skill
- **Full user flow tests** → use `write-e2e-tests` skill
