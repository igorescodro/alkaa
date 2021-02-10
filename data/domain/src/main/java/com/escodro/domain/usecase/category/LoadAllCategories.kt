package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category

/**
 * Use case to load all categories from the database.
 */
interface LoadAllCategories {
    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    suspend operator fun invoke(): List<Category>
}
