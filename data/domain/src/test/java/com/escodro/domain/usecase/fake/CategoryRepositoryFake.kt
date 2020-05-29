package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import java.util.TreeMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class CategoryRepositoryFake : CategoryRepository {

    private val categoryMap: TreeMap<Long, Category> = TreeMap()

    override suspend fun insertCategory(category: Category) {
        val id = if (category.id == 0L && categoryMap.isEmpty()) {
            1
        } else if (category.id == 0L) {
            categoryMap.lastKey() + 1
        } else {
            category.id
        }

        categoryMap[id] = category
    }

    override suspend fun insertCategory(categoryList: List<Category>) {
        categoryList.forEach { category -> insertCategory(category) }
    }

    override suspend fun updateCategory(category: Category) {
        categoryMap[category.id] = category
    }

    override suspend fun deleteCategory(category: Category) {
        categoryMap.remove(category.id)
    }

    override suspend fun cleanTable() {
        categoryMap.clear()
    }

    override fun findAllCategories(): Flow<List<Category>> =
        flow {
            emit(categoryMap.values.toList())
        }

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryMap[categoryId]
}
