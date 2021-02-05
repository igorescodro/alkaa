package com.escodro.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.escodro.local.model.Task

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
    suspend fun insertTask(task: Task)

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * Cleans the entire table.
     */
    @Query("DELETE FROM task")
    suspend fun cleanTable()

    /**
     * Get all inserted tasks with due date.
     *
     * @return all inserted tasks with due date
     */
    @Query("SELECT * FROM task where task_due_date is not null")
    suspend fun findAllTasksWithDueDate(): List<Task>

    /**
     * Get task by id.
     *
     * @param taskId task id
     *
     * @return selected task
     */
    @Query("SELECT * FROM task WHERE task_id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?
}
