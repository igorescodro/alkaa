package com.escodro.alkaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.escodro.alkaa.data.local.model.Category
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
    fun findTaskByName(name: String): Single<Category>
}
