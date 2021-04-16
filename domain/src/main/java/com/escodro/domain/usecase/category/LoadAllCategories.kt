package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Use case to load all categories from the database.
 */
interface LoadAllCategories {
    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    operator fun invoke(): Flow<List<Category>>
}
