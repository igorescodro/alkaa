package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

/**
 * Use case to load a specific category from the database.
 */
interface LoadCategory {

    /**
     * Loads the category based on the given id.
     *
     * @param categoryId category id
     *
     * @return an single observable to be subscribed
     */
    suspend operator fun invoke(categoryId: Long): Category?
}
