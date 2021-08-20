package com.escodro.repository.mapper

import com.escodro.domain.model.Task as DomainTask
import com.escodro.repository.model.Task as RepoTask

/**
 * Maps Tasks between Repository and Domain.
 */
internal class TaskMapper(private val alarmIntervalMapper: AlarmIntervalMapper) {

    /**
     * Maps Task from Repo to Domain.
     *
     * @param repoTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toDomain(repoTask: RepoTask): DomainTask =
        DomainTask(
            id = repoTask.id,
            completed = repoTask.completed,
            title = repoTask.title,
            description = repoTask.description,
            categoryId = repoTask.categoryId,
            dueDate = repoTask.dueDate,
            creationDate = repoTask.creationDate,
            completedDate = repoTask.completedDate,
            isRepeating = repoTask.isRepeating,
            alarmInterval = repoTask.alarmInterval?.let { alarmIntervalMapper.toDomain(it) }
        )

    /**
     * Maps Task from Domain to Repo.
     *
     * @param domainTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun toRepo(domainTaskList: List<DomainTask>): List<RepoTask> =
        domainTaskList.map { toRepo(it) }

    /**
     * Maps Task from Domain to Repo.
     *
     * @param domainTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toRepo(domainTask: DomainTask): RepoTask =
        RepoTask(
            id = domainTask.id,
            completed = domainTask.completed,
            title = domainTask.title,
            description = domainTask.description,
            categoryId = domainTask.categoryId,
            dueDate = domainTask.dueDate,
            creationDate = domainTask.creationDate,
            completedDate = domainTask.completedDate,
            isRepeating = domainTask.isRepeating,
            alarmInterval = domainTask.alarmInterval?.let { alarmIntervalMapper.toRepo(it) }
        )
}
