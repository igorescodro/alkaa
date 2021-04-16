package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

/**
 * Use case to insert a category in the database.
 */
interface AddCategory {

    /**
     * Inserts a category.
     *
     * @param category category to be inserted
     */
    suspend operator fun invoke(category: Category)
}
