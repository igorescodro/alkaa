package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.LoadAllCategories

internal class LoadAllCategoriesImpl(
    private val categoryRepository: CategoryRepository
) : LoadAllCategories {

    override suspend operator fun invoke(): List<Category> =
        categoryRepository.findAllCategories()
}
