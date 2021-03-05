package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.AddTask
import timber.log.Timber

internal class AddTaskImpl(private val taskRepository: TaskRepository) : AddTask {

    override suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            Timber.e("Task cannot be inserted with a empty title")
            return
        }

        taskRepository.insertTask(task)
    }
}
