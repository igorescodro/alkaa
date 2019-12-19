package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository

/**
 * Use case to save or update a category in the database.
 */
class UpsertCategory(private val categoryRepository: CategoryRepository) {

    /**
     * Adds or updates a category.
     *
     * @param category category to be added or updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(category: Category) {
        val isNewCategory = category.id == 0L

        if (isNewCategory) {
            categoryRepository.insertCategory(category)
        } else {
            categoryRepository.updateCategory(category)
        }
    }
}
