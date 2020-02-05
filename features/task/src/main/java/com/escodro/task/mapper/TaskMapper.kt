package com.escodro.task.mapper

import com.escodro.domain.model.Task as DomainTask
import com.escodro.task.model.Task as ViewTask

/**
 * Maps Tasks between Domain and View.
 */
internal class TaskMapper(private val alarmIntervalMapper: AlarmIntervalMapper) {

    /**
     * Maps Task from Domain to View.
     *
     * @param domainTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toView(domainTask: DomainTask): ViewTask =
        ViewTask(
            id = domainTask.id,
            completed = domainTask.completed,
            title = domainTask.title,
            description = domainTask.description,
            dueDate = domainTask.dueDate,
            categoryId = domainTask.categoryId,
            creationDate = domainTask.creationDate,
            completedDate = domainTask.completedDate,
            isRepeating = domainTask.isRepeating,
            alarmInterval = alarmIntervalMapper.toViewData(domainTask.alarmInterval)
        )

    /**
     * Maps Task from View to Domain.
     *
     * @param viewTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toDomain(viewTask: ViewTask): DomainTask =
        DomainTask(
            id = viewTask.id,
            completed = viewTask.completed,
            title = viewTask.title,
            description = viewTask.description,
            categoryId = viewTask.categoryId,
            dueDate = viewTask.dueDate,
            creationDate = viewTask.creationDate,
            completedDate = viewTask.completedDate,
            isRepeating = viewTask.isRepeating,
            alarmInterval = viewTask.alarmInterval?.let { alarmIntervalMapper.toDomain(it) }
        )
}
