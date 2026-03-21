---
name: write-feature
description: Use when adding a new feature to Alkaa end-to-end — from domain models through data layer, Koin DI, and up to presentation. Triggers on tasks like "add a new feature", "create [feature] feature", "implement [feature] functionality", or "add [feature] to the app".
---

# Write Feature

## Overview

A complete Alkaa feature spans five layers: domain models → repository interface → data implementation → Koin DI → presentation. Each layer has strict conventions. This skill guides you through all five in order and points to specialized skills for presentation and tests.

**Before starting:** Use `superpowers:brainstorming` to align on scope and API design. Use `superpowers:writing-plans` if the feature is large.

## Phase 1: Domain Layer

### 1a. Domain Model

Location: `features/<name>-api/src/commonMain/kotlin/com/escodro/<name>api/model/`

```kotlin
// Immutable data class. Nullable only if genuinely optional.
data class Category(
    val id: Long,
    val name: String,
    val color: Long,
    val parentId: Long? = null,  // nullable only when optional
)
```

### 1b. Repository Interface

Location: `domain/src/commonMain/kotlin/com/escodro/domain/repository/`

```kotlin
// Flow<T> for streams, suspend fun for single-shot mutations. No Result wrapping.
interface CategoryRepository {
    fun findAllCategories(): Flow<List<Category>>
    fun findCategoryById(id: Long): Flow<Category>
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(id: Long)
}
```

### 1c. Use Case Interface

Location: `domain/src/commonMain/kotlin/com/escodro/domain/usecase/<feature>/`

```kotlin
// operator fun invoke() for callable syntax. One interface = one action.
interface LoadAllCategories {
    operator fun invoke(): Flow<List<Category>>
}
```

### 1d. Use Case Implementation

Location: `domain/src/commonMain/kotlin/com/escodro/domain/usecase/<feature>/implementation/`

```kotlin
// internal. Business logic lives here. Simple cases are pass-throughs.
internal class LoadAllCategoriesImpl(
    private val categoryRepository: CategoryRepository,
) : LoadAllCategories {
    override operator fun invoke(): Flow<List<Category>> =
        categoryRepository.findAllCategories()
}
```

**Rules:**
- `internal` visibility — never `public`
- One injected dependency per use case (typically the repository)
- Never inject another use case or ViewModel
- If logic is simple, a pass-through is correct and expected

## Phase 2: Data Layer

> If the feature requires a **new database table**, invoke the `write-local-datasource` skill before continuing. It covers the full local stack: `.sq` schema, DAO, local mapper, LocalDataSource, and DI in `LocalModule`.

### 2a. Repository Mapper

Location: `data/repository/src/commonMain/kotlin/com/escodro/repository/mapper/`

```kotlin
// Three-layer conversion: Local Entity ↔ Domain Model
// For composite types (e.g., TaskWithCategory), inject sub-mappers as constructor params
internal class CategoryMapper {
    fun toDomain(entity: CategoryEntity): Category =
        Category(id = entity.categoryId, name = entity.categoryName, color = entity.categoryColor)

    fun toRepo(domain: Category): CategoryEntity =
        CategoryEntity(categoryId = domain.id, categoryName = domain.name, categoryColor = domain.color)
}
```

Reference files:
- Simple: `data/repository/src/commonMain/kotlin/com/escodro/repository/mapper/CategoryMapper.kt`
- Composite: `data/repository/src/commonMain/kotlin/com/escodro/repository/mapper/TaskWithCategoryMapper.kt`

### 2b. Repository Implementation

Location: `data/repository/src/commonMain/kotlin/com/escodro/repository/`

```kotlin
// internal. Inject DataSource + Mapper. Map Flow here, not in the DAO.
internal class CategoryRepositoryImpl(
    private val localDataSource: CategoryLocalDataSource,
    private val mapper: CategoryMapper,
) : CategoryRepository {
    override fun findAllCategories(): Flow<List<Category>> =
        localDataSource.findAllCategories().map { list -> list.map { mapper.toDomain(it) } }

    override suspend fun insertCategory(category: Category) =
        localDataSource.insertCategory(mapper.toRepo(category))
}
```

Reference files:
- `data/repository/src/commonMain/kotlin/com/escodro/repository/CategoryRepositoryImpl.kt`

## Phase 3: Koin DI

### 3a. Feature Module

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/di/<Name>Module.kt`

```kotlin
val categoryModule = module {
    // ViewModels
    viewModelOf(::CategoryListViewModelImpl)

    // Use cases — always bind to interface
    factoryOf(::LoadAllCategoriesImpl) bind LoadAllCategories::class
    factoryOf(::DeleteCategoryImpl) bind DeleteCategory::class

    // Mappers — no interface binding needed
    factoryOf(::CategoryMapper)
    factoryOf(::CategoryViewMapper)

    // Navigation
    factoryOf(::CategoryNavGraph) bind NavGraph::class

    // Platform-specific (only if needed)
    includes(platformCategoryModule)
}
```

### 3b. Repository Module

Add to `data/repository/src/commonMain/kotlin/com/escodro/repository/di/RepositoryModule.kt`:

```kotlin
singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
```

### 3c. Register in KoinHelper

Add `categoryModule` to the `appModules` list in:
`shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`

**Do this immediately** — omitting it causes silent runtime crashes.

Reference files:
- Feature module: `features/task/src/commonMain/kotlin/com/escodro/task/di/TaskModule.kt`
- KoinHelper: `shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`

## Phase 4: Presentation Layer

### 4a. View Model (UI data class)

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/model/`

Separate from domain model — allows UI-specific fields without polluting domain.

### 4b. View Mapper (Domain → View)

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/mapper/`

```kotlin
internal class CategoryViewMapper {
    fun toView(domain: Category): CategoryViewData =
        CategoryViewData(id = domain.id, name = domain.name, colorHex = domain.color.toColorHex())
}
```

### 4c. Remaining Presentation

- **ViewModel** → use `write-viewmodel` skill
- **Composable + Screens** → use `write-composable` skill
- **Navigation** → use `navigation` skill
- **Strings** → use `localization` skill

## Phase 5: Tests

- Use case unit tests → use `write-unit-tests` skill
- ViewModel unit tests → use `write-unit-tests` skill
- UI / composable tests → use `write-ui-tests` skill
- End-to-end flow tests → use `write-e2e-tests` skill

## Feature API Module Design

A `<name>-api` module is **only needed when another feature will depend on this one**. Default to a single module and add the API split only when cross-feature sharing is required.

**In `<name>-api`** (no implementations allowed):
- Abstract ViewModel base classes
- Sealed state classes shared across features
- View model data classes exposed to other features
- Screen interfaces (when a feature injects a screen into another feature's navigation)

**In `<name>`** (private to the feature):
- Concrete ViewModel implementations
- Composables and screen implementations
- Mappers (repository and view)
- NavGraph implementation
- Koin module

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Using `suspend fun` for a stream | Use `Flow<T>` for streams; `suspend fun` is only for one-shot mutations |
| Wrapping return values in `Result<T>` | Don't. Exceptions propagate through Flow — the ViewModel catches with `.catch {}` |
| One use case doing two things | One use case = one action. Create separate use cases. |
| Skipping the mapper and returning the entity | Never. Local ↔ repo ↔ domain separation is required. |
| Injecting a repository into the ViewModel | ViewModels inject use cases only — never repositories directly |
| Feature depending directly on another feature module | Create a `<name>-api` module for the shared interface and depend only on that |
| Registering in KoinHelper after the feature is working | Register immediately — missing registration causes silent runtime crashes |
| Reusing the domain model class as the view model | Keep them separate so UI concerns don't bleed into domain |
