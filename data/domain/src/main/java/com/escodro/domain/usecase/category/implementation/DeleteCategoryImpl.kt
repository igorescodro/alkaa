package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.DeleteCategory

internal class DeleteCategoryImpl(
    private val categoryRepository: CategoryRepository
) : DeleteCategory {

    override suspend operator fun invoke(category: Category) =
        categoryRepository.deleteCategory(category)
}
