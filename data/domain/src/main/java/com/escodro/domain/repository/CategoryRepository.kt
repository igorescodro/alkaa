package com.escodro.domain.repository

import com.escodro.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Interface to represent the implementation of Category repository.
 */
interface CategoryRepository {

    /**
     * Inserts a new category.
     *
     * @param category category to be added
     */
    suspend fun insertCategory(category: Category)

    /**
     * Inserts a new category list.
     *
     * @param categoryList list of category to be added
     */
    suspend fun insertCategory(categoryList: List<Category>)

    /**
     * Updates the given category.
     *
     * @param category category to be updated
     */
    suspend fun updateCategory(category: Category)

    /**
     * Deletes a category.
     *
     * @param category task to be deleted
     */
    suspend fun deleteCategory(category: Category)

    /**
     * Cleans the entire table.
     */
    suspend fun cleanTable()

    /**
     * Get all inserted categories.
     *
     * @return all inserted categories.
     */
    fun findAllCategories(): Flow<List<Category>>

    /**
     * Gets a specific category by id.
     *
     * @param categoryId category id
     */
    suspend fun findCategoryById(categoryId: Long): Category?
}
