package com.escodro.task.mapper

import com.escodro.designsystem.components.kuvio.chip.KuvioTaskChipType
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemData
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemState
import com.escodro.domain.model.TaskWithCategory
import com.escodro.task.presentation.v2.TaskItem
import com.escodro.task.presentation.v2.TaskSectionType
import com.escodro.task.provider.RelativeDateTimeProvider

internal class TaskItemMapper(
    private val relativeDateTimeProvider: RelativeDateTimeProvider,
) {

    fun toTaskItem(taskWithCategory: TaskWithCategory, sectionType: TaskSectionType): TaskItem {
        val kuvioState = when (sectionType) {
            TaskSectionType.OVERDUE -> KuvioTaskItemState.OVERDUE
            TaskSectionType.COMPLETED -> KuvioTaskItemState.COMPLETED
            else -> KuvioTaskItemState.PENDING
        }
        val dateChip = taskWithCategory.task.dueDate?.let { dueDate ->
            val label = relativeDateTimeProvider.toRelativeDateTimeString(dueDate)
            when (sectionType) {
                TaskSectionType.OVERDUE, TaskSectionType.TODAY -> KuvioTaskChipType.DateToday(label)
                TaskSectionType.UPCOMING -> KuvioTaskChipType.DateSoon(label)
                TaskSectionType.COMPLETED -> KuvioTaskChipType.DateLater(label)
                TaskSectionType.NO_DATE -> null
            }
        }
        return TaskItem(
            id = taskWithCategory.task.id,
            kuvioData = KuvioTaskItemData(
                title = taskWithCategory.task.title,
                state = kuvioState,
                chips = listOfNotNull(dateChip),
            ),
        )
    }
}
