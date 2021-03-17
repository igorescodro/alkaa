package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

interface UpdateCategory {
    /**
     * Updates a category.
     *
     * @param category category to be updated
     */
    suspend operator fun invoke(category: Category)
}
