package com.escodro.alkaa.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.Flowable

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
    fun findTaskByName(name: String): Category
}
