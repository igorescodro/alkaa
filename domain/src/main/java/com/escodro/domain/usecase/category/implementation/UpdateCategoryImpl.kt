package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.UpdateCategory

internal class UpdateCategoryImpl(
    private val categoryRepository: CategoryRepository
) : UpdateCategory {

    override suspend operator fun invoke(category: Category) {
        categoryRepository.updateCategory(category)
    }
}
