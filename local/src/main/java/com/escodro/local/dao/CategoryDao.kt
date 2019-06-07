package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.escodro.model.Category
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * DAO class to handle all [Category]-related database operations.
 */
@Dao
interface CategoryDao {

    /**
     * Get all inserted categories.
     *
     * @return all inserted categories.
     */
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flowable<MutableList<Category>>

    /**
     * Inserts a new category.
     *
     * @param category category to be added
     */
    @Insert(onConflict = REPLACE)
    fun insertCategory(category: Category)

    /**
     * Inserts a new category list.
     *
     * @param category list of category to be added
     */
    @Insert(onConflict = REPLACE)
    fun insertCategory(category: List<Category>)

    /**
     * Updates the given category.
     *
     * @param category category to be updated
     */
    @Update
    fun updateCategory(category: Category)

    /**
     * Deletes a category.
     *
     * @param category task to be deleted
     */
    @Delete
    fun deleteCategory(category: Category)

    /**
     * Cleans the entire table.
     */
    @Query("DELETE FROM category")
    fun cleanTable()

    /**
     * Gets a specific category by name.
     *
     * @param name category name
     */
    @Query("SELECT * FROM category WHERE category_name = :name")
    fun findCategoryByName(name: String): Single<Category>

    /**
     * Gets a specific category by id.
     *
     * @param categoryId category id
     */
    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    fun findCategory(categoryId: Long): Single<Category>
}
