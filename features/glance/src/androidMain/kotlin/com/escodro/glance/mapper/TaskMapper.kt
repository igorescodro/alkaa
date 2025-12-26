package com.escodro.glance.mapper

import com.escodro.domain.model.TaskWithCategory
import com.escodro.glance.model.Task

internal class TaskMapper {

    fun toView(localTaskList: List<TaskWithCategory>): List<Task> =
        localTaskList.map { toView(it) }

    private fun toView(taskWithCategory: TaskWithCategory): Task =
        Task(id = taskWithCategory.task.id, title = taskWithCategory.task.title)
}
