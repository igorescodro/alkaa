package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

/**
 * Use case to delete a category from the database.
 */
interface DeleteCategory {

    /**
     * Deletes a category.
     *
     * @param category category to be deleted
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(category: Category)
}
