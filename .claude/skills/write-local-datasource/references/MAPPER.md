# Local Mapper

**Location:** `data/local/src/commonMain/kotlin/com/escodro/local/mapper/<Name>Mapper.kt`

This mapper sits in `data/local` and converts between the **SQLDelight-generated type** and the **repository model**. It is distinct from the `data/repository` mapper, which converts repository model ↔ domain model.

```kotlin
// toRepo: SQLDelight local type → repository model
// fromRepo: repository model → SQLDelight local type
internal class CategoryMapper {
    fun toRepo(local: Category): com.escodro.repository.model.Category =
        com.escodro.repository.model.Category(
            id = local.category_id,
            name = local.category_name,
            color = local.category_color,
        )

    fun fromRepo(repo: com.escodro.repository.model.Category): Category =
        Category(
            category_id = repo.id,
            category_name = repo.name,
            category_color = repo.color,
        )
}
```

**Distinguishing the two mapper layers:**

| Mapper | Location | Converts |
|--------|----------|----------|
| `data/local/mapper/<Name>Mapper` | `data/local` | SQLDelight type ↔ repository model (`toRepo` / `fromRepo`) |
| `data/repository/mapper/<Name>Mapper` | `data/repository` | repository model ↔ domain model (`toDomain` / `toRepo`) |
