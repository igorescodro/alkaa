package com.escodro.domain.usecase.checklist

import com.escodro.domain.model.ChecklistItem
import com.escodro.domain.repository.ChecklistRepository
import kotlinx.coroutines.flow.Flow

class LoadChecklistItems(private val checklistRepository: ChecklistRepository) {
    operator fun invoke(taskId: Long): Flow<List<ChecklistItem>> =
        checklistRepository.getChecklistItems(taskId)
}
