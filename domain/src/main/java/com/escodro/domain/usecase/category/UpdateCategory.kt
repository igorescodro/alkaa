package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

/**
 * Use case to update a category in the database.
 */
interface UpdateCategory {
    /**
     * Updates a category.
     *
     * @param category category to be updated
     */
    suspend operator fun invoke(category: Category)
}
