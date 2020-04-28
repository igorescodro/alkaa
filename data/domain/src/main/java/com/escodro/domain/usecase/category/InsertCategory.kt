package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository

/**
 * Use case to insert a category in the database.
 */
class InsertCategory(private val categoryRepository: CategoryRepository) {

    /**
     * Inserts a category.
     *
     * @param category category to be inserted
     */
    suspend operator fun invoke(category: Category) {
        categoryRepository.insertCategory(category)
    }
}
