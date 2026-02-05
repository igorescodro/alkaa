package com.escodro.local.datasource

import com.escodro.local.dao.ChecklistDao
import com.escodro.local.mapper.ChecklistItemMapper
import com.escodro.repository.datasource.ChecklistDataSource
import com.escodro.repository.model.ChecklistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChecklistLocalDataSource(
    private val checklistDao: ChecklistDao,
    private val checklistMapper: ChecklistItemMapper,
) : ChecklistDataSource {

    override suspend fun insertChecklistItem(item: ChecklistItem) =
        checklistDao.insertChecklistItem(checklistMapper.toLocal(item))

    override suspend fun updateChecklistItem(item: ChecklistItem) {
        checklistDao.updateChecklistItem(checklistMapper.toLocal(item))
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        checklistDao.deleteChecklistItem(checklistMapper.toLocal(item))
    }

    override fun getChecklistItems(taskId: Long): Flow<List<ChecklistItem>> =
        checklistDao.getChecklistItems(taskId).map { list ->
            list.map { checklistMapper.toRepo(it) }
        }
}
