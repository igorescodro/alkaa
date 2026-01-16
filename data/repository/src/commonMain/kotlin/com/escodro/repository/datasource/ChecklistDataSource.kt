package com.escodro.repository.datasource

import com.escodro.repository.model.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface ChecklistDataSource {
    suspend fun insertChecklistItem(item: ChecklistItem)
    suspend fun updateChecklistItem(item: ChecklistItem)
    suspend fun deleteChecklistItem(item: ChecklistItem)
    fun getChecklistItems(taskId: Long): Flow<List<ChecklistItem>>
}
