package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UpdateTaskCategory

internal class UpdateTaskCategoryFake : UpdateTaskCategory {

    private val updatedMap = HashMap<Long, Long?>()

    override suspend fun invoke(taskId: Long, categoryId: Long?) {
        updatedMap[taskId] = categoryId
    }

    fun isCategoryUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedCategory(taskId: Long): Long? =
        updatedMap[taskId]
}
