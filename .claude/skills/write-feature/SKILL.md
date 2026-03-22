---
name: write-feature
description: Use when adding a new feature to Alkaa end-to-end — from domain models through data layer, Koin DI, and up to presentation. Triggers on tasks like "add a new feature", "create [feature] feature", "implement [feature] functionality", or "add [feature] to the app".
---

# Write Feature

## Overview

A complete Alkaa feature spans five layers: domain models → repository interface → data implementation → Koin DI → presentation. Each layer has strict conventions. This skill guides you through all five in order.

**Before starting:** Use `superpowers:brainstorming` to align on scope and API design. Use `superpowers:writing-plans` if the feature is large.

## Phases

1. **Domain Layer** — Domain model, repository interface, use case interface + impl → see `references/DOMAIN_LAYER.md`
2. **Data Layer** — Repository mapper + RepositoryImpl → see `references/DATA_LAYER.md`
   - If the feature needs a new database table, invoke the `write-local-datasource` skill first
3. **Koin DI** — Feature module, RepositoryModule, KoinHelper registration → see `references/KOIN_DI.md`
4. **Presentation** — ViewModel, Composables, Navigation, strings → see delegation below
5. **Tests** — Unit, UI, E2E → see delegation below

## Rules

| Rule | Details |
|------|---------|
| **Use cases are internal** | `internal` visibility — never `public` |
| **One use case = one action** | Never inject another use case or ViewModel |
| **Flow vs. suspend** | `Flow<T>` for streams; `suspend fun` for one-shot mutations. No `Result<T>` wrapping |
| **Mappers required** | Never return the entity directly — local ↔ repo ↔ domain separation is required |
| **ViewModels inject use cases** | Never inject repositories into ViewModels |
| **Register in KoinHelper immediately** | Missing registration causes silent runtime crashes |
| **API module only when shared** | Only create `<name>-api` when another feature depends on this one → see `references/FEATURE_API_MODULE.md` |

## Phase 4: Presentation

- **ViewModel** → use `write-viewmodel` skill
- **Composables + Screens** → use `write-composable` skill
- **Navigation** → use `navigation` skill
- **Strings** → use `localization` skill
- **New Kuvio component needed** → use `write-design-system-component` skill before implementing the composable

## Phase 5: Tests

- Use case / ViewModel unit tests → use `write-unit-tests` skill
- UI / composable tests → use `write-ui-tests` skill
- End-to-end flow tests → use `write-e2e-tests` skill

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `suspend fun` for a stream | Use `Flow<T>` for streams; `suspend` only for one-shot mutations |
| Wrapping return values in `Result<T>` | Don't — exceptions propagate through Flow |
| One use case doing two things | Create separate use cases |
| Skipping the mapper | Local ↔ repo ↔ domain separation is required |
| Injecting a repository into the ViewModel | ViewModels inject use cases only |
| Feature depending directly on another feature | Create a `<name>-api` module |
| Registering in KoinHelper after the feature works | Register immediately — silent runtime crashes |
| Reusing the domain model as the view model | Keep them separate — UI concerns must not bleed into domain |
