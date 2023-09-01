package com.escodro.local.mapper

import com.escodro.local.Category
import com.escodro.local.SelectAllTasksWithCategory
import com.escodro.local.Task
import com.escodro.local.model.TaskWithCategory

internal class SelectMapper {
    fun toTaskWithCategory(list: List<SelectAllTasksWithCategory>): List<TaskWithCategory> =
        list.map { result ->
            TaskWithCategory(
                task = Task(
                    task_id = result.task_id,
                    task_is_completed = result.task_is_completed,
                    task_title = result.task_title,
                    task_description = result.task_description,
                    task_category_id = result.task_category_id,
                    task_due_date = result.task_due_date,
                    task_creation_date = result.task_creation_date,
                    task_completed_date = result.task_completed_date,
                    task_is_repeating = result.task_is_repeating,
                    task_alarm_interval = result.task_alarm_interval,
                ),
                category = if (result.category_id != null &&
                    result.category_name != null &&
                    result.category_color != null
                ) {
                    Category(
                        category_id = result.category_id,
                        category_name = result.category_name,
                        category_color = result.category_color,
                    )
                } else {
                    null
                },
            )
        }
}
