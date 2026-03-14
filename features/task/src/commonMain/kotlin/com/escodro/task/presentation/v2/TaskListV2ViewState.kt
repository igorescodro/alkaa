package com.escodro.task.presentation.v2

import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemData
import kotlinx.collections.immutable.ImmutableList

internal sealed class TaskListV2ViewState {

    data object Loading : TaskListV2ViewState()

    data class Error(val cause: Throwable) : TaskListV2ViewState()

    data class Loaded(
        val categoryName: String,
        val categoryEmoji: String,
        val totalCount: Int,
        val completedCount: Int,
        val sections: ImmutableList<TaskSection>,
        val addTaskText: String,
    ) : TaskListV2ViewState()
}

internal data class TaskSection(
    val type: TaskSectionType,
    val tasks: ImmutableList<TaskItem>,
)

internal enum class TaskSectionType { OVERDUE, TODAY, UPCOMING, COMPLETED, NO_DATE }

internal data class TaskItem(
    val id: Long,
    val kuvioData: KuvioTaskItemData,
)
