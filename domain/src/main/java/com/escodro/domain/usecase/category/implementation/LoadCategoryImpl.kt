package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.LoadCategory

internal class LoadCategoryImpl(private val categoryRepository: CategoryRepository) : LoadCategory {

    override suspend operator fun invoke(categoryId: Long): Category? =
        categoryRepository.findCategoryById(categoryId)
}
