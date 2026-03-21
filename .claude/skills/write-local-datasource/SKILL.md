---
name: write-local-datasource
description: Use when a new feature needs to persist data locally in Alkaa — triggers on tasks like "add database support", "create a new table", "store this data in SQLDelight", or when write-feature Phase 2 requires a new entity in the local database.
---

# Write Local DataSource

## Overview

The local data layer has six artifacts per entity: a SQLDelight `.sq` schema, a DataSource interface (in `data/repository`), a DAO interface, a DAO implementation, a local mapper, and a LocalDataSource implementation. All six must follow strict conventions; the DI module wires them together at the end.

## Phase 1: DataSource Interface

Location: `data/repository/src/commonMain/kotlin/com/escodro/repository/datasource/<Name>DataSource.kt`

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

## Phase 2: SQLDelight Schema (.sq)

Location: `data/local/src/commonMain/sqldelight/com/escodro/local/<Name>.sq`

```sql
-- Import custom Kotlin types at the top
import kotlin.Boolean;
import kotlinx.datetime.LocalDateTime;

CREATE TABLE IF NOT EXISTS Category (
    `category_id`    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `category_name`  TEXT NOT NULL,
    `category_color` TEXT NOT NULL
);

selectAll:
SELECT * FROM Category;

selectByCategoryId:
SELECT * FROM Category WHERE category_id = ?;

insert:
INSERT INTO Category (category_name, category_color) VALUES (?, ?);

update:
UPDATE Category SET category_name = ?, category_color = ? WHERE category_id = ?;

delete:
DELETE FROM Category WHERE category_id = ?;

cleanTable:
DELETE FROM Category;
```

**Rules:**
- Field names prefixed with the table name in snake_case (e.g., `category_id`, `task_title`)
- Custom Kotlin types declared via `INTEGER AS MyType` with a matching adapter in `DatabaseProvider`
- Every table needs `insert:`, `update:`, `delete:`, and `cleanTable:` — `cleanTable:` is required for E2E test teardown
- FOREIGN KEY with `ON DELETE CASCADE` for child tables (e.g., Task referencing Category)
- For inserts that need to return the new ID: add a `lastInsertedId: SELECT LAST_INSERT_ROWID();` query

## Phase 3: DAO Interface

Location: `data/local/src/commonMain/kotlin/com/escodro/local/dao/<Name>Dao.kt`

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

## Phase 4: DAO Implementation

Location: `data/local/src/commonMain/kotlin/com/escodro/local/dao/impl/<Name>DaoImpl.kt`

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

## Phase 5: Local Mapper

Location: `data/local/src/commonMain/kotlin/com/escodro/local/mapper/<Name>Mapper.kt`

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

## Phase 6: LocalDataSource Implementation

Location: `data/local/src/commonMain/kotlin/com/escodro/local/datasource/<Name>LocalDataSource.kt`

Implements the DataSource interface from Phase 1. Injects DAO + local mapper. Never accesses `*Queries` directly.

```kotlin
internal class CategoryLocalDataSource(
    private val categoryDao: CategoryDao,
    private val categoryMapper: CategoryMapper,
) : CategoryDataSource {

    override fun findAllCategories(): Flow<List<com.escodro.repository.model.Category>> =
        categoryDao.findAllCategories().map { list -> list.map { categoryMapper.toRepo(it) } }

    override suspend fun findCategoryById(categoryId: Long): com.escodro.repository.model.Category? =
        categoryDao.findCategoryById(categoryId)?.let { categoryMapper.toRepo(it) }

    override suspend fun insertCategory(category: com.escodro.repository.model.Category) {
        categoryDao.insertCategory(categoryMapper.fromRepo(category))
    }

    override suspend fun updateCategory(category: com.escodro.repository.model.Category) {
        categoryDao.updateCategory(categoryMapper.fromRepo(category))
    }

    override suspend fun deleteCategory(category: com.escodro.repository.model.Category) {
        categoryDao.deleteCategory(categoryMapper.fromRepo(category))
    }

    override suspend fun cleanTable() {
        categoryDao.cleanTable()
    }
}
```

## Phase 7: DI Registration

Add to `data/local/src/commonMain/kotlin/com/escodro/local/di/LocalModule.kt`:

```kotlin
val localModule = module {
    // DataSources — singleOf, bind to interface
    singleOf(::CategoryLocalDataSource) bind CategoryDataSource::class

    // Mappers — factoryOf, no interface binding
    factoryOf(::CategoryMapper)

    // DAOs — singleOf, bind to interface
    singleOf(::CategoryDaoImpl) bind CategoryDao::class

    // DatabaseProvider — already registered; do NOT add again
    includes(platformLocalModule)
}
```

**DI rules:**
- `singleOf` for DataSources, DAOs, and DatabaseProvider — they hold or share database state
- `factoryOf` for Mappers — stateless, cheap to create, no interface binding needed

## Custom Type Adapters

If the new schema uses `INTEGER AS MyType`, register the adapter in `DatabaseProvider.createDatabase()`:

```kotlin
val database = AlkaaDatabase(
    driver = driver,
    TaskAdapter = Task.Adapter(
        task_due_dateAdapter = dateTimeAdapter,
        task_alarm_intervalAdapter = alarmIntervalAdapter,
    ),
)
```

Adapters live in `data/local/src/commonMain/kotlin/com/escodro/local/converter/`.

Reference files:
- `data/local/src/commonMain/sqldelight/com/escodro/local/Category.sq`
- `data/local/src/commonMain/kotlin/com/escodro/local/dao/CategoryDao.kt`
- `data/local/src/commonMain/kotlin/com/escodro/local/dao/impl/CategoryDaoImpl.kt`
- `data/local/src/commonMain/kotlin/com/escodro/local/mapper/CategoryMapper.kt`
- `data/local/src/commonMain/kotlin/com/escodro/local/datasource/CategoryLocalDataSource.kt`
- `data/local/src/commonMain/kotlin/com/escodro/local/di/LocalModule.kt`

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `suspend fun findAll()` in DAO when the repository exposes a `Flow` | Use `fun findAll(): Flow<List<T>>` — no suspend for reactive reads |
| Calling `.first()` in DaoImpl for a `Flow` return | Return `Flow` directly; callers decide when to collect |
| Using `executeAsOne()` for single reads | Always `executeAsOneOrNull()` — assume nullable |
| Omitting `cleanTable:` in the `.sq` file | Required for E2E tests to reset state between runs |
| Putting the local mapper in `data/repository/mapper/` | Local mapper belongs in `data/local/mapper/` with `toRepo`/`fromRepo` methods |
| Registering DataSource or DAO as `factoryOf` | Always `singleOf` — they share database state across the app |
| Adding `DatabaseProvider` registration again | It is already registered once; duplicate registration causes a Koin conflict |
| Accessing `*Queries` directly in LocalDataSource | LocalDataSource injects the DAO — never the queries object |
