package com.escodro.alkaa.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import io.reactivex.Flowable
import io.reactivex.Single
import org.jetbrains.annotations.TestOnly

/**
 * DAO class to handle all [Task]-related database operations.
 */
@Dao
interface TaskDao {

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
     * @param isCompleted `false` to show all the completed tasks, `false` otherwise
     *
     * @return all inserted tasks with category
     */
    @Query(
        "SELECT * FROM task LEFT JOIN category ON task_category_id = category_id " +
            "WHERE task_is_completed = :isCompleted"
    )
    fun getAllTasksWithCategory(isCompleted: Boolean): Flowable<MutableList<TaskWithCategory>>

    /**
     * Get all inserted tasks related with the given category.
     *
     * @param categoryId the category id
     *
     * @return all inserted tasks with category
     */
    @Query(
        "SELECT * FROM task LEFT JOIN category ON task_category_id = category_id " +
            "WHERE task_category_id = :categoryId and task_is_completed = 0"
    )
    fun getAllTasksWithCategoryId(categoryId: Long): Flowable<MutableList<TaskWithCategory>>

    /**
     * Get all inserted tasks with due date.
     *
     * @return all inserted tasks with due date
     */
    @Query("SELECT * FROM task where task_due_date is not null")
    fun getAllTasksWithDueDate(): Single<List<Task>>

    /**
     * Get all inserted tasks.
     *
     * @return all inserted tasks
     */
    @TestOnly
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flowable<MutableList<Task>>

    /**
     * Gets a specific task by title.
     *
     * @param title task title
     */
    @TestOnly
    @Query("SELECT * FROM task WHERE task_title = :title")
    fun findTaskByTitle(title: String): Single<Task>
}
