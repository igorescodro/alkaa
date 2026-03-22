# Domain Layer Reference

## 1a. Domain Model

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

## 1b. Repository Interface

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

## 1c. Use Case Interface

Location: `domain/src/commonMain/kotlin/com/escodro/domain/usecase/<feature>/`

```kotlin
// operator fun invoke() for callable syntax. One interface = one action.
interface LoadAllCategories {
    operator fun invoke(): Flow<List<Category>>
}
```

## 1d. Use Case Implementation

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
- Inject repositories, interactors and other use cases, as needed
- If logic is simple, a pass-through is correct and expected
- When a use case triggers actions in another feature (e.g., cancel an alarm, dismiss a
  notification), use an **Interactor** — see `references/INTERACTOR.md`
