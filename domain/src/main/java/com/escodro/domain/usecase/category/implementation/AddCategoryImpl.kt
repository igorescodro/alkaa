package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.AddCategory
import mu.KLogging

internal class AddCategoryImpl(
    private val categoryRepository: CategoryRepository
) : AddCategory {

    override suspend operator fun invoke(category: Category) {
        if (category.name.isBlank()) {
            logger.debug { "Category cannot be inserted with a empty name" }
            return
        }
        categoryRepository.insertCategory(category)
    }

    companion object : KLogging()
}
