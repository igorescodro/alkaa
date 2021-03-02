package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Use case to add a task from the database.
 */
class AddTask @Inject constructor(private val taskRepository: TaskRepository) {

    /**
     * Adds a task.
     *
     * @param task the task to be added
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            Timber.e("Task cannot be inserted with a empty title")
            return
        }

        taskRepository.insertTask(task)
    }
}
