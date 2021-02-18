package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskDescription

internal class UpdateTaskDescriptionImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask
) : UpdateTaskDescription {

    override suspend fun invoke(taskId: Long, description: String) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(description = description)
        updateTask(updatedTask)
    }
}
