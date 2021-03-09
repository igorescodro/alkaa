package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.escodro.local.model.Category
import kotlinx.coroutines.flow.Flow

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
    fun findAllCategories(): Flow<List<Category>>

    /**
     * Inserts a new category.
     *
     * @param category category to be added
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertCategory(category: Category)

    /**
     * Inserts a new category list.
     *
     * @param category list of category to be added
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertCategory(category: List<Category>)

    /**
     * Updates the given category.
     *
     * @param category category to be updated
     */
    @Update
    suspend fun updateCategory(category: Category)

    /**
     * Deletes a category.
     *
     * @param category task to be deleted
     */
    @Delete
    suspend fun deleteCategory(category: Category)

    /**
     * Cleans the entire table.
     */
    @Query("DELETE FROM category")
    suspend fun cleanTable()

    /**
     * Gets a specific category by name.
     *
     * @param name category name
     */
    @Query("SELECT * FROM category WHERE category_name = :name")
    suspend fun findCategoryByName(name: String): Category

    /**
     * Gets a specific category by id.
     *
     * @param categoryId category id
     */
    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    suspend fun findCategoryById(categoryId: Long): Category?
}
