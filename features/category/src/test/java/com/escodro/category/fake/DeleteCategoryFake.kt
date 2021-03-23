package com.escodro.category.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.DeleteCategory

internal class DeleteCategoryFake : DeleteCategory {

    private val updatedList: MutableList<Category> = mutableListOf()

    override suspend fun invoke(category: Category) {
        updatedList.add(category)
    }

    fun clear() =
        updatedList.clear()

    fun wasCategoryRemoved(id: Long): Boolean =
        updatedList.any { it.id == id }
}
