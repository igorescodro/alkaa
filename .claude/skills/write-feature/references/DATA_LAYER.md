# Data Layer Reference

## Phase 2: Data Layer

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

## Phase 4a: View Model (UI data class)

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/model/`

Separate from domain model — allows UI-specific fields without polluting domain.

## Phase 4b: View Mapper (Domain → View)

Location: `features/<name>/src/commonMain/kotlin/com/escodro/<name>/mapper/`

```kotlin
internal class CategoryViewMapper {
    fun toView(domain: Category): CategoryViewData =
        CategoryViewData(id = domain.id, name = domain.name, colorHex = domain.color.toColorHex())
}
```
