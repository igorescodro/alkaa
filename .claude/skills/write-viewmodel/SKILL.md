---
name: write-viewmodel
description: Use when writing a new ViewModel or modifying an existing one in the Alkaa project. Triggers on tasks like "add a ViewModel", "create VM for X screen", "implement state handling", or "connect use case to UI".
---

# Write ViewModel

## Overview

ViewModels in Alkaa bridge domain use cases and Compose UI via cold flows and sealed state classes. They contain no business logic — only orchestration, mapping, and scope management.

## Structure

1. **Sealed State Class** — `data object` for stateless states, `data class` for states with data, `ImmutableList` for list payloads → see `references/CODE_PATTERNS.md`
2. **Flow Exposure** — Cold `Flow<ViewState>` by default; hot `StateFlow` only when multiple collectors share the same stream → see `references/CODE_PATTERNS.md`
3. **Use Case Injection** — Inject use cases as constructor parameters; never repositories or other ViewModels → see `references/CODE_PATTERNS.md`
4. **Coroutine Scope** — `applicationScope` for DB mutations that survive ViewModel destruction; `viewModelScope` for UI-bound work → see `references/CODE_PATTERNS.md`
5. **Mappers** — Dedicated mapper class injected as constructor param; never map inline → see `references/CODE_PATTERNS.md`

For unit test setup and fake patterns → see `references/CODE_PATTERNS.md` and the `write-unit-tests` skill.

## Scope Rules

| Situation | Scope |
|-----------|-------|
| DB insert / update / delete | `applicationScope.launch { }` |
| Network call that should persist | `applicationScope.launch { }` |
| Debounced UI updates | `viewModelScope` |
| Short-lived UI-bound work | `viewModelScope` |

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Calling repository directly | Wrap in a use case first |
| Exposing `StateFlow` for a screen-scoped stream | Return cold `Flow<ViewState>` |
| Mapping models inline in ViewModel | Create a `XxxMapper` class |
| Using `viewModelScope` for DB writes | Use `applicationScope.launch { }` |
| Depending on another ViewModel | Extract shared logic into a use case |
| Testing with mocks | Write a `XxxFake` that implements the use case interface |

## Related Skills

- **Fake patterns and test structure** → use `write-unit-tests` skill
- **Composable and screen wiring** → use `write-composable` skill
