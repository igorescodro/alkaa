package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.escodro.local.model.Task
import io.reactivex.Completable
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
    fun insertTask(task: Task): Completable

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    @Update
    fun updateTask(task: Task): Completable

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    @Delete
    fun deleteTask(task: Task): Completable

    /**
     * Cleans the entire table.
     */
    @Query("DELETE FROM task")
    fun cleanTable(): Completable

    /**
     * Get all inserted tasks with due date.
     *
     * @return all inserted tasks with due date
     */
    @Query("SELECT * FROM task where task_due_date is not null")
    fun findAllTasksWithDueDate(): Single<List<Task>>

    /**
     * Get task by id.
     *
     * @param taskId task id
     *
     * @return selected task
     */
    @Query("SELECT * FROM task WHERE task_id = :taskId")
    fun getTaskById(taskId: Long): Single<Task>

    /**
     * Get all inserted tasks.
     *
     * @return all inserted tasks
     */
    @TestOnly
    @Query("SELECT * FROM task")
    fun findAllTasks(): Flowable<MutableList<Task>>

    /**
     * Gets a specific task by title.
     *
     * @param title task title
     *
     * @return selected task
     */
    @TestOnly
    @Query("SELECT * FROM task WHERE task_title = :title")
    fun findTaskByTitle(title: String): Single<Task>
}
