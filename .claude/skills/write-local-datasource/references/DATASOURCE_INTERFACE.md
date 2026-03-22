# DataSource Interface

**Location:** `data/repository/src/commonMain/kotlin/com/escodro/repository/datasource/<Name>DataSource.kt`

This is the contract the `RepositoryImpl` in `write-feature` depends on. Mirror the DAO signature but use **repository models**, not local types.

```kotlin
// Flow for observable streams, suspend for single reads and mutations
interface CategoryDataSource {
    fun findAllCategories(): Flow<List<Category>>
    suspend fun findCategoryById(categoryId: Long): Category?
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun cleanTable()
}
```

**Rules:**
- Use `Flow<List<T>>` (not suspend) for reactive/observable reads
- Use `suspend` for mutations and point-in-time single reads
- All types here are **repository models** — never SQLDelight-generated types
