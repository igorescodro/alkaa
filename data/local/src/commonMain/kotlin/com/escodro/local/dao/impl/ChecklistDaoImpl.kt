package com.escodro.local.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.escodro.coroutines.CoroutineDispatcherProvider
import com.escodro.local.ChecklistItem
import com.escodro.local.ChecklistItemQueries
import com.escodro.local.dao.ChecklistDao
import com.escodro.local.provider.DatabaseProvider
import kotlinx.coroutines.flow.Flow

internal class ChecklistDaoImpl(
    private val databaseProvider: DatabaseProvider,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : ChecklistDao {

    private val checklistItemQueries: ChecklistItemQueries
        get() = databaseProvider.getInstance().checklistItemQueries

    override suspend fun insertChecklistItem(item: ChecklistItem) {
        checklistItemQueries.insertItem(
            item_task_id = item.item_task_id,
            item_title = item.item_title,
            item_is_completed = item.item_is_completed,
        )
    }

    override suspend fun updateChecklistItem(item: ChecklistItem) {
        checklistItemQueries.updateItem(
            item_title = item.item_title,
            item_is_completed = item.item_is_completed,
            item_id = item.item_id,
        )
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        checklistItemQueries.deleteItem(item.item_id)
    }

    override fun getChecklistItems(taskId: Long): Flow<List<ChecklistItem>> {
        return checklistItemQueries.selectByTaskId(taskId)
            .asFlow()
            .mapToList(dispatcherProvider.io)
    }
}
