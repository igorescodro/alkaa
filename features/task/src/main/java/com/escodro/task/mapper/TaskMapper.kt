package com.escodro.task.mapper

import com.escodro.core.extension.toCalendar
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
            dueDate = domainTask.dueDate?.toCalendar(),
            categoryId = domainTask.categoryId,
            creationDate = domainTask.creationDate?.toCalendar(),
            completedDate = domainTask.completedDate?.toCalendar(),
            isRepeating = domainTask.isRepeating,
            alarmInterval = alarmIntervalMapper.toViewData(domainTask.alarmInterval),
        )
}
