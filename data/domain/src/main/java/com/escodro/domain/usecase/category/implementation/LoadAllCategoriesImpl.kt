package com.escodro.domain.usecase.category.implementation

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.usecase.category.LoadAllCategories
import kotlinx.coroutines.flow.Flow

internal class LoadAllCategoriesImpl(
    private val categoryRepository: CategoryRepository
) : LoadAllCategories {

    override operator fun invoke(): Flow<List<Category>> =
        categoryRepository.findAllCategories()
}
