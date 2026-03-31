package com.escodro.repository.mapper

import com.escodro.domain.model.ChecklistItem as DomainChecklistItem
import com.escodro.repository.model.ChecklistItem as RepoChecklistItem

internal class ChecklistItemMapper {

    fun toRepo(domain: DomainChecklistItem): RepoChecklistItem =
        RepoChecklistItem(
            id = domain.id,
            taskId = domain.taskId,
            title = domain.title,
            isCompleted = domain.isCompleted,
        )

    fun toDomain(repo: RepoChecklistItem): DomainChecklistItem =
        DomainChecklistItem(
            id = repo.id,
            taskId = repo.taskId,
            title = repo.title,
            isCompleted = repo.isCompleted,
        )
}
