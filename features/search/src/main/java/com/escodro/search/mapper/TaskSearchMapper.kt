package com.escodro.search.mapper

import androidx.compose.ui.graphics.Color
import com.escodro.domain.model.TaskWithCategory
import com.escodro.search.model.TaskSearchItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class TaskSearchMapper {

    fun toTaskSearch(taskList: List<TaskWithCategory>): ImmutableList<TaskSearchItem> =
        taskList.map(::toTaskSearch).toImmutableList()

    private fun toTaskSearch(taskWithCategory: TaskWithCategory): TaskSearchItem =
        TaskSearchItem(
            id = taskWithCategory.task.id,
            completed = taskWithCategory.task.completed,
            title = taskWithCategory.task.title,
            categoryColor = getColor(taskWithCategory.category?.color),
            isRepeating = taskWithCategory.task.isRepeating
        )

    private fun getColor(color: String?): Color? {
        if (color == null) {
            return null
        }

        return Color(android.graphics.Color.parseColor(color))
    }
}
