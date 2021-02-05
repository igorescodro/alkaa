package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.LoadTask

internal class LoadTaskImpl(private val taskRepository: TaskRepository) : LoadTask {

    override suspend operator fun invoke(taskId: Long): Task? =
        taskRepository.findTaskById(taskId)
}
