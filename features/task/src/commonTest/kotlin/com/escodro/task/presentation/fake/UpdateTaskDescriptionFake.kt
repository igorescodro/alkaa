package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UpdateTaskDescription

internal class UpdateTaskDescriptionFake : UpdateTaskDescription {

    private val updatedMap = HashMap<Long, String>()

    override suspend fun invoke(taskId: Long, description: String) {
        updatedMap[taskId] = description
    }

    fun isDescriptionUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedDescription(taskId: Long): String? =
        updatedMap[taskId]
}
