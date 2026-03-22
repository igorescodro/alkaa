# Koin DI Reference

## 3a. Feature Module

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

## 3b. Repository Module

Add to `data/repository/src/commonMain/kotlin/com/escodro/repository/di/RepositoryModule.kt`:

```kotlin
singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
```

## 3c. Register in KoinHelper

Add `categoryModule` to the `appModules` list in:
`shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`

**Do this immediately** — omitting it causes silent runtime crashes.

## Reference files

- Feature module: `features/task/src/commonMain/kotlin/com/escodro/task/di/TaskModule.kt`
- KoinHelper: `shared/src/commonMain/kotlin/com/escodro/shared/di/KoinHelper.kt`
