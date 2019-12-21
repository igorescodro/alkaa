package com.escodro.repository.datasource

import com.escodro.repository.model.Task
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.TestOnly

/**
 * Interface to represent the implementation of Task data source.
 */
interface TaskDataSource {

    /**
     * Inserts a new task.
     *
     * @param task task to be added
     */
    suspend fun insertTask(task: Task)

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
    suspend fun findTaskById(taskId: Long): Task

    /**
     * Get all inserted tasks.
     *
     * @return all inserted tasks
     */
    @TestOnly
    fun findAllTasks(): Flow<List<Task>>

    /**
     * Gets a specific task by title.
     *
     * @param title task title
     *
     * @return selected task
     */
    @TestOnly
    suspend fun findTaskByTitle(title: String): Task
}
