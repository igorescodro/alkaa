package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class CategoryRepositoryFake : CategoryRepository {

    private val categoryMap: MutableMap<Long, Category> = mutableMapOf()

    override suspend fun insertCategory(category: Category) {
        val id = if (category.id == 0L && categoryMap.isEmpty()) {
            1
        } else if (category.id == 0L) {
            categoryMap.entries
                .maxByOrNull { it.key }
                ?.key
                ?.plus(1) ?: 1
        } else {
            category.id
        }

        categoryMap[id] = category
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
        flowOf(categoryMap.values.toList())

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryMap[categoryId]
}
