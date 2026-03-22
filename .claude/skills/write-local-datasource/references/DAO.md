# DAO Interface and Implementation

## DAO Interface

**Location:** `data/local/src/commonMain/kotlin/com/escodro/local/dao/<Name>Dao.kt`

```kotlin
interface CategoryDao {
    // Observable streams: return Flow — never suspend
    fun findAllCategories(): Flow<List<Category>>

    // Single reads: suspend, always nullable
    suspend fun findCategoryById(categoryId: Long): Category?

    // Mutations: suspend, Unit return
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun cleanTable()
}
```

**Rule:** `Flow<List<T>>` (not suspend) for reactive reads; `suspend` for mutations and one-shot reads. If the repository only needs a point-in-time snapshot (not a stream), use `suspend fun ...: List<T>` instead.

---

## DAO Implementation

**Location:** `data/local/src/commonMain/kotlin/com/escodro/local/dao/impl/<Name>DaoImpl.kt`

```kotlin
internal class CategoryDaoImpl(
    private val databaseProvider: DatabaseProvider,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : CategoryDao {

    private val categoryQueries: CategoryQueries
        get() = databaseProvider.getInstance().categoryQueries

    // Flow return: .asFlow().mapToList() — no .first()
    override fun findAllCategories(): Flow<List<Category>> =
        categoryQueries.selectAll().asFlow().mapToList(dispatcherProvider.io)

    // Suspend return: .executeAsOneOrNull() — never executeAsOne()
    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryQueries.selectByCategoryId(categoryId).executeAsOneOrNull()

    override suspend fun insertCategory(category: Category) {
        categoryQueries.insert(
            category_name = category.category_name,
            category_color = category.category_color,
        )
    }

    override suspend fun updateCategory(category: Category) {
        categoryQueries.update(
            category_name = category.category_name,
            category_color = category.category_color,
            category_id = category.category_id,
        )
    }

    override suspend fun deleteCategory(category: Category) {
        categoryQueries.delete(category.category_id)
    }

    override suspend fun cleanTable() {
        categoryQueries.cleanTable()
    }
}
```

For inserts that must return a generated ID, use `transactionWithResult`:

```kotlin
override suspend fun insertTask(task: Task): Long =
    taskQueries.transactionWithResult {
        taskQueries.insert(/* fields */)
        taskQueries.lastInsertedId().executeAsOne()
    }
```

**Rules:**
- `*Queries` accessed via a lazy `get()` property delegating to `databaseProvider.getInstance()`
- Always use named parameters in query calls — never rely on positional order
- Flow reads: `.asFlow().mapToList(dispatcherProvider.io)` without `.first()`
- Suspend list reads (point-in-time): `.asFlow().mapToList(dispatcherProvider.io).first()`
