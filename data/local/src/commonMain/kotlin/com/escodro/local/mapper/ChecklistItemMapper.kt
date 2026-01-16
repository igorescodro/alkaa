package com.escodro.local.mapper

import com.escodro.repository.model.ChecklistItem as RepoChecklistItem
import com.escodro.local.ChecklistItem as LocalChecklistItem

internal class ChecklistItemMapper {

    fun toRepo(local: LocalChecklistItem): RepoChecklistItem =
        RepoChecklistItem(
            id = local.item_id,
            taskId = local.item_task_id,
            title = local.item_title,
            isCompleted = local.item_is_completed,
        )

    fun toLocal(repo: RepoChecklistItem): LocalChecklistItem =
        LocalChecklistItem(
            item_id = repo.id,
            item_task_id = repo.taskId,
            item_title = repo.title,
            item_is_completed = repo.isCompleted,
        )
}
