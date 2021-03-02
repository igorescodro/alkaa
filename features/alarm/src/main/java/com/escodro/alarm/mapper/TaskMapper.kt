package com.escodro.alarm.mapper

import javax.inject.Inject
import com.escodro.alarm.model.Task as ViewTask
import com.escodro.domain.model.Task as DomainTask

/**
 * Maps Tasks between View and Domain.
 */
internal class TaskMapper @Inject constructor() {

    /**
     * Maps Task from Domain to View.
     *
     * @param domainTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun fromDomain(domainTaskList: List<DomainTask>): List<ViewTask> =
        domainTaskList.map { fromDomain(it) }

    /**
     * Maps Task from Domain to View.
     *
     * @param domainTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun fromDomain(domainTask: DomainTask): ViewTask =
        ViewTask(
            id = domainTask.id,
            title = domainTask.title,
            dueDate = domainTask.dueDate,
            isCompleted = domainTask.completed
        )
}
