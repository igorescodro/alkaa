package com.escodro.repository.datasource

import com.escodro.repository.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
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
    fun insertTask(task: Task): Completable

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    fun updateTask(task: Task): Completable

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    fun deleteTask(task: Task): Completable

    /**
     * Cleans the entire table.
     */
    fun cleanTable(): Completable

    /**
     * Get all inserted tasks with due date.
     *
     * @return all inserted tasks with due date
     */
    fun findAllTasksWithDueDate(): Single<List<Task>>

    /**
     * Get all inserted tasks.
     *
     * @return all inserted tasks
     */
    @TestOnly
    fun findAllTasks(): Flowable<List<Task>>

    /**
     * Gets a specific task by title.
     *
     * @param title task title
     *
     * @return selected task
     */
    @TestOnly
    fun findTaskByTitle(title: String): Single<Task>
}
