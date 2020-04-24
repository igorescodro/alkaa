package com.escodro.search.mapper

import android.graphics.Color
import com.escodro.domain.model.TaskWithCategory
import com.escodro.search.model.TaskSearch

/**
 * Maps Tracker between View and Domain.
 */
internal class TaskSearchMapper {

    /**
     * Maps between View and Domain.
     */
    fun toTaskSearch(taskList: List<TaskWithCategory>): List<TaskSearch> =
        taskList.map(::toTaskSearch)

    private fun toTaskSearch(taskWithCategory: TaskWithCategory): TaskSearch =
        TaskSearch(
            id = taskWithCategory.task.id,
            completed = taskWithCategory.task.completed,
            title = taskWithCategory.task.title,
            categoryColor = taskWithCategory.category?.color?.let { Color.parseColor(it) },
            isRepeating = taskWithCategory.task.isRepeating
        )
}
