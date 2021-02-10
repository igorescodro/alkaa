package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import java.util.TreeMap

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

    override suspend fun findAllCategories(): List<Category> =
        categoryMap.values.toList()

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryMap[categoryId]
}
