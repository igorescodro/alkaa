package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to load all categories from the database.
 */
class LoadAllCategories(private val categoryRepository: CategoryRepository) {

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    operator fun invoke(): Flow<List<Category>> =
        categoryRepository.findAllCategories()
}
