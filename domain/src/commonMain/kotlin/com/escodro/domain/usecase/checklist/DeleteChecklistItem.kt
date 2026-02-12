package com.escodro.domain.usecase.checklist

import com.escodro.domain.model.ChecklistItem
import com.escodro.domain.repository.ChecklistRepository

class DeleteChecklistItem(private val checklistRepository: ChecklistRepository) {
    suspend operator fun invoke(item: ChecklistItem) =
        checklistRepository.deleteChecklistItem(item)
}
