package com.escodro.local.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.escodro.local.Category
import com.escodro.local.CategoryQueries
import com.escodro.local.dao.CategoryDao
import com.escodro.local.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

internal class CategoryDaoImpl(private val databaseProvider: DatabaseProvider) : CategoryDao {

    private val categoryQueries: CategoryQueries
        get() = databaseProvider.getInstance().categoryQueries

    override fun findAllCategories(): Flow<List<Category>> =
        categoryQueries.selectAll().asFlow().mapToList(Dispatchers.IO)

    override suspend fun insertCategory(category: Category) {
        categoryQueries.insert(
            category_name = category.category_name,
            category_color = category.category_color,
        )
    }

    override suspend fun insertCategory(categoryList: List<Category>) {
        for (category in categoryList) {
            insertCategory(category)
        }
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

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryQueries.selectByCategoryId(categoryId).executeAsOneOrNull()
}
