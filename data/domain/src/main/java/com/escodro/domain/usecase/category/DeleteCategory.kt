package com.escodro.domain.usecase.category

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository

/**
 * Use case to delete a category from the database.
 */
class DeleteCategory(private val categoryRepository: CategoryRepository) {

    /**
     * Deletes a category.
     *
     * @param category category to be deleted
     *
     * @return observable to be subscribe
     */
    operator fun invoke(category: Category) =
        categoryRepository.deleteCategory(category).applySchedulers()
}
