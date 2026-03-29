package com.escodro.domain.usecase.checklist

import com.escodro.domain.model.ChecklistItem
import com.escodro.domain.repository.ChecklistRepository

class UpdateChecklistItem(private val checklistRepository: ChecklistRepository) {
    suspend operator fun invoke(item: ChecklistItem) =
        checklistRepository.updateChecklistItem(item)
}
