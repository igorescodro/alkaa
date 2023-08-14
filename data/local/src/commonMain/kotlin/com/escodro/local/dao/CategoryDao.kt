package com.escodro.local.dao

import com.escodro.local.Category
import kotlinx.coroutines.flow.Flow

/**
 * DAO class to handle all [Category]-related database operations.
 */
internal interface CategoryDao {

    /**
     * Get all inserted categories.
     *
     * @return all inserted categories.
     */
    fun findAllCategories(): Flow<List<Category>>

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
     * Gets a specific category by id.
     *
     * @param categoryId category id
     */
    suspend fun findCategoryById(categoryId: Long): Category?
}
