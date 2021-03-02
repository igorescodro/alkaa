package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.LoadAllCategories
import javax.inject.Inject

internal class LoadAllCategoriesImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : LoadAllCategories {

    override suspend operator fun invoke(): List<Category> =
        categoryRepository.findAllCategories()
}
