package com.escodro.repository

import com.escodro.domain.model.ChecklistItem
import com.escodro.domain.repository.ChecklistRepository
import com.escodro.repository.datasource.ChecklistDataSource
import com.escodro.repository.mapper.ChecklistItemMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChecklistRepositoryImpl(
    private val checklistDataSource: ChecklistDataSource,
    private val checklistMapper: ChecklistItemMapper,
) : ChecklistRepository {

    override suspend fun insertChecklistItem(item: ChecklistItem) {
        checklistDataSource.insertChecklistItem(checklistMapper.toRepo(item))
    }

    override suspend fun updateChecklistItem(item: ChecklistItem) {
        checklistDataSource.updateChecklistItem(checklistMapper.toRepo(item))
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        checklistDataSource.deleteChecklistItem(checklistMapper.toRepo(item))
    }

    override fun getChecklistItems(taskId: Long): Flow<List<ChecklistItem>> {
        return checklistDataSource.getChecklistItems(taskId).map { list ->
            list.map { checklistMapper.toDomain(it) }
        }
    }
}
