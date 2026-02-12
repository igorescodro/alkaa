package com.escodro.domain.repository

import com.escodro.domain.model.ChecklistItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface to represent the implementation of Checklist repository.
 */
interface ChecklistRepository {

    /**
     * Inserts a new checklist item.
     *
     * @param item checklist item to be added
     */
    suspend fun insertChecklistItem(item: ChecklistItem)

    /**
     * Updates a checklist item.
     *
     * @param item checklist item to be updated
     */
    suspend fun updateChecklistItem(item: ChecklistItem)

    /**
     * Deletes a checklist item.
     *
     * @param item checklist item to be deleted
     */
    suspend fun deleteChecklistItem(item: ChecklistItem)

    /**
     * Gets the checklist items from a task.
     *
     * @param taskId the task id
     *
     * @return the checklist items from the task
     */
    fun getChecklistItems(taskId: Long): Flow<List<ChecklistItem>>
}
