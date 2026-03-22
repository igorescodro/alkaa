# LocalDataSource Implementation

**Location:** `data/local/src/commonMain/kotlin/com/escodro/local/datasource/<Name>LocalDataSource.kt`

Implements the DataSource interface from Phase 1. Injects DAO + local mapper. Never accesses `*Queries` directly.

```kotlin
internal class CategoryLocalDataSource(
    private val categoryDao: CategoryDao,
    private val categoryMapper: CategoryMapper,
) : CategoryDataSource {

    override fun findAllCategories(): Flow<List<com.escodro.repository.model.Category>> =
        categoryDao.findAllCategories().map { categoryMapper.toRepo(it) }

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
