package com.escodro.alkaa.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import io.reactivex.Flowable

/**
 * DAO class to handle all [Task]-related database operations.
 */
@Dao
interface TaskDao {

    /**
     * Get all inserted tasks.
     *
     * @return all inserted tasks.
     */
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flowable<MutableList<Task>>

    /**
     * Gets a specific task by id.
     *
     * @param id task id
     */
    @Query("SELECT * FROM task WHERE task_id = :id")
    fun findTaskById(id: Long): Task

    /**
     * Inserts a new task.
     *
     * @param task task to be added
     */
    @Insert(onConflict = REPLACE)
    fun insertTask(task: Task)

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    @Update
    fun updateTask(task: Task)

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    @Delete
    fun deleteTask(task: Task)

    /**
     * Cleans the entire table.
     */
    @Query("DELETE FROM task")
    fun cleanTable()

    /**
     * Get all inserted tasks with category.
     *
     * @return all inserted tasks with category.
     */
    @Query("SELECT * FROM task LEFT JOIN category ON task_category_id = category_id")
    fun getAllTasksWithCategory(): Flowable<MutableList<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category.
     */
    @Query(
        "SELECT * FROM task LEFT JOIN category ON task_category_id = category_id " +
            "WHERE task_category_id = :categoryId"
    )
    fun getAllTasksWithCategoryId(categoryId: Long): Flowable<MutableList<TaskWithCategory>>
}
