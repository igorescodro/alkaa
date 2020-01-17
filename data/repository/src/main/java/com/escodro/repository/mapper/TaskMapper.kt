package com.escodro.repository.mapper

import com.escodro.domain.model.Task as DomainTask
import com.escodro.repository.model.Task as RepoTask

/**
 * Maps Tasks between Repository and Domain.
 */
internal class TaskMapper {

    /**
     * Maps Task from Repo to Domain.
     *
     * @param localTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun toDomain(localTaskList: List<RepoTask>): List<DomainTask> =
        localTaskList.map { toDomain(it) }

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
            isRepeating = false, /* TODO update mapper*/
            alarmInterval = null /* TODO update mapper*/
        )

    /**
     * Maps Task from Domain to Repo.
     *
     * @param localTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun toRepo(localTaskList: List<DomainTask>): List<RepoTask> =
        localTaskList.map { toRepo(it) }

    /**
     * Maps Task from Domain to Repo.
     *
     * @param localTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toRepo(localTask: DomainTask): RepoTask =
        RepoTask(
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
