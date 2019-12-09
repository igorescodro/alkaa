package com.escodro.repository.datasource

import com.escodro.repository.model.Category
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Interface to represent the implementation of Category data source.
 */
interface CategoryDataSource {

    /**
     * Inserts a new category.
     *
     * @param category category to be added
     */
    fun insertCategory(category: Category): Completable

    /**
     * Inserts a new category list.
     *
     * @param category list of category to be added
     */
    fun insertCategory(category: List<Category>): Completable

    /**
     * Updates the given category.
     *
     * @param category category to be updated
     */
    fun updateCategory(category: Category): Completable

    /**
     * Deletes a category.
     *
     * @param category task to be deleted
     */
    fun deleteCategory(category: Category): Completable

    /**
     * Cleans the entire table.
     */
    fun cleanTable(): Completable

    /**
     * Get all inserted categories.
     *
     * @return all inserted categories.
     */
    fun findAllCategories(): Flowable<List<Category>>

    /**
     * Gets a specific category by name.
     *
     * @param name category name
     */
    fun findCategoryByName(name: String): Single<Category>

    /**
     * Gets a specific category by id.
     *
     * @param categoryId category id
     */
    fun findCategoryById(categoryId: Long): Single<Category>
}
