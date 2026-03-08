package com.escodro.task.mapper

import com.escodro.domain.model.TaskWithCategory
import com.escodro.task.presentation.v2.TaskItem

internal class TaskItemMapper {

    fun toTaskItem(taskWithCategory: TaskWithCategory): TaskItem =
        TaskItem(
            id = taskWithCategory.task.id,
            title = taskWithCategory.task.title,
            isCompleted = taskWithCategory.task.isCompleted,
            dueDate = taskWithCategory.task.dueDate,
        )
}
