package com.escodro.task.presentation.list.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.UpdateTask

internal class UpdateTaskFake : UpdateTask {

    private val updatedList: MutableList<Task> = mutableListOf()

    override suspend fun invoke(task: Task) {
        updatedList.add(task)
    }

    fun isTaskUpdated(taskId: Long): Boolean =
        updatedList.any { it.id == taskId }

    fun getUpdatedTask(taskId: Long): Task? =
        updatedList.find { it.id == taskId }
}
