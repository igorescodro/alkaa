package com.escodro.category.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.AddCategory

internal class AddCategoryFake : AddCategory {

    private val updatedList: MutableList<Category> = mutableListOf()

    override suspend fun invoke(category: Category) {
        updatedList.add(category)
    }

    fun clear() =
        updatedList.clear()

    fun wasCategoryCreated(title: String): Boolean =
        updatedList.any { it.name == title }

    fun getCategory(title: String): Category? =
        updatedList.find { it.name == title }
}
