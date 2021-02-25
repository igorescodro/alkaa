package com.escodro.search.mapper

import android.graphics.Color
import com.escodro.domain.model.TaskWithCategory
import com.escodro.search.model.TaskSearchItem

internal class TaskSearchMapper {

    fun toTaskSearch(taskList: List<TaskWithCategory>): List<TaskSearchItem> =
        taskList.map(::toTaskSearch)

    private fun toTaskSearch(taskWithCategory: TaskWithCategory): TaskSearchItem =
        TaskSearchItem(
            id = taskWithCategory.task.id,
            completed = taskWithCategory.task.completed,
            title = taskWithCategory.task.title,
            categoryColor = taskWithCategory.category?.color?.let { Color.parseColor(it) },
            isRepeating = taskWithCategory.task.isRepeating
        )
}
