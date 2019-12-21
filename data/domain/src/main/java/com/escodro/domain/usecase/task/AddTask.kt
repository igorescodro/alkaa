package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to add a task from the database.
 */
class AddTask(private val taskRepository: TaskRepository) {

    /**
     * Adds a task.
     *
     * @param task the task to be added
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) =
        taskRepository.insertTask(task)
}
