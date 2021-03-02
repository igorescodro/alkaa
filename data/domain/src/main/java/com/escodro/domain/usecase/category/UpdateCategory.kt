package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * Use case to update a category in the database.
 */
class UpdateCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     * Updates a category.
     *
     * @param category category to be updated
     */
    suspend operator fun invoke(category: Category) {
        categoryRepository.updateCategory(category)
    }
}
