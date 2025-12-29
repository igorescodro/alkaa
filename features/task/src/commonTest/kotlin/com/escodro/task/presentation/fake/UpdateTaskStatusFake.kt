package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UpdateTaskStatus

internal class UpdateTaskStatusFake : UpdateTaskStatus {

    private val updatedList: MutableList<Long> = mutableListOf()

    override suspend fun invoke(taskId: Long) {
        updatedList.add(taskId)
    }

    fun isTaskUpdated(taskId: Long): Boolean =
        updatedList.contains(taskId)
}
