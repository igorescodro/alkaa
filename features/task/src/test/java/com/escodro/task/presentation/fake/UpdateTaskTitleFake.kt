package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UpdateTaskTitle

internal class UpdateTaskTitleFake : UpdateTaskTitle {

    private val updatedMap = HashMap<Long, String>()

    override suspend fun invoke(taskId: Long, title: String) {
        updatedMap[taskId] = title
    }

    fun isTitleUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedTitle(taskId: Long): String? =
        updatedMap[taskId]
}
