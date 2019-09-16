package com.escodro.domain.mapper

import com.escodro.domain.viewdata.ViewData
import com.escodro.model.Task

/**
 * Converts between the [Task] model from the database and [ViewData.Task] UI object.
 */
class TaskMapper {

    /**
     * Maps from a [Task] to [ViewData.Task].
     *
     * @param task object to be mapped
     *
     * @return the converted object
     */
    fun toViewTask(task: Task) =
        ViewData.Task(
            id = task.id,
            completed = task.completed,
            title = task.title,
            description = task.description,
            categoryId = task.categoryId,
            dueDate = task.dueDate,
            creationDate = task.creationDate,
            completedDate = task.completedDate
        )

    /**
     * Maps from a [ViewData.Task] to [Task].
     *
     * @param task object to be mapped
     *
     * @return the converted object
     */
    fun toEntityTask(task: ViewData.Task) =
        Task(
            id = task.id,
            completed = task.completed,
            title = task.title,
            description = task.description,
            categoryId = task.categoryId,
            dueDate = task.dueDate,
            creationDate = task.creationDate,
            completedDate = task.completedDate
        )
}
