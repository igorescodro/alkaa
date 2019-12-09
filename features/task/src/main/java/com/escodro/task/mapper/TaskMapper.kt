package com.escodro.task.mapper

import com.escodro.domain.model.Task as DomainTask
import com.escodro.task.model.Task as ViewTask

/**
 * Maps Tasks between Domain and View.
 */
internal class TaskMapper {

    /**
     * Maps Task from Domain to View.
     *
     * @param repoTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toView(repoTask: DomainTask): ViewTask =
        ViewTask(
            id = repoTask.id,
            completed = repoTask.completed,
            title = repoTask.title,
            description = repoTask.description,
            dueDate = repoTask.dueDate,
            categoryId = repoTask.categoryId,
            creationDate = repoTask.creationDate,
            completedDate = repoTask.completedDate
        )

    /**
     * Maps Task from View to Domain.
     *
     * @param localTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toDomain(localTask: ViewTask): DomainTask =
        DomainTask(
            id = localTask.id,
            completed = localTask.completed,
            title = localTask.title,
            description = localTask.description,
            categoryId = localTask.categoryId,
            dueDate = localTask.dueDate,
            creationDate = localTask.creationDate,
            completedDate = localTask.completedDate
        )
}
