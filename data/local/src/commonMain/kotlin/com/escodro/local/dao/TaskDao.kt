package com.escodro.local.dao

import com.escodro.local.Task

/**
 * DAO class to handle all [Task]-related database operations.
 */
interface TaskDao {

    /**
     * Inserts a new task.
     *
     * @param task task to be added
     *
     * @return the id of the inserted task
     */
    suspend fun insertTask(task: Task): Long

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    suspend fun deleteTask(task: Task)

    /**
     * Cleans the entire table.
     */
    suspend fun cleanTable()

    /**
     * Get all inserted tasks with due date.
     *
     * @return all inserted tasks with due date
     */
    suspend fun findAllTasksWithDueDate(): List<Task>

    /**
     * Get task by id.
     *
     * @param taskId task id
     *
     * @return selected task
     */
    suspend fun getTaskById(taskId: Long): Task?

    /**
     * Get task by id as flow.
     *
     * @param taskId task id
     *
     * @return selected task
     */
    fun getTaskByIdFlow(taskId: Long): kotlinx.coroutines.flow.Flow<Task?>
}
