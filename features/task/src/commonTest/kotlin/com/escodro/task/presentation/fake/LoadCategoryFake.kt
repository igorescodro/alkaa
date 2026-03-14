package com.escodro.task.presentation.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.LoadCategory

internal class LoadCategoryFake : LoadCategory {

    private val categories = mutableMapOf<Long, Category>()

    fun addCategory(category: Category) {
        categories[category.id] = category
    }

    fun clear() {
        categories.clear()
    }

    override suspend fun invoke(categoryId: Long): Category? = categories[categoryId]
}
