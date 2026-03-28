package com.escodro.category.fake

import com.escodro.domain.usecase.task.UpdateTaskStatus

internal class UpdateTaskStatusFake : UpdateTaskStatus {
    val updatedIds = mutableListOf<Long>()

    override suspend fun invoke(taskId: Long) {
        updatedIds.add(taskId)
    }

    fun clear() {
        updatedIds.clear()
    }
}
