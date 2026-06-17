package com.escodro.task.mapper

import com.escodro.domain.model.ChecklistItem as DomainChecklistItem
import com.escodro.task.model.ChecklistItem as ViewChecklistItem

internal class ChecklistItemMapper {

    fun toView(domain: DomainChecklistItem): ViewChecklistItem =
        ViewChecklistItem(
            id = domain.id,
            taskId = domain.taskId,
            title = domain.title,
            isCompleted = domain.isCompleted,
        )

    fun toDomain(view: ViewChecklistItem): DomainChecklistItem =
        DomainChecklistItem(
            id = view.id,
            taskId = view.taskId,
            title = view.title,
            isCompleted = view.isCompleted,
        )
}
