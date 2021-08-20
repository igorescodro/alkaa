package com.escodro.local.mapper

import com.escodro.local.model.Task as LocalTask
import com.escodro.repository.model.Task as RepoTask

/**
 * Maps Tasks between Repository and Local.
 */
internal class TaskMapper(private val alarmIntervalMapper: AlarmIntervalMapper) {

    /**
     * Maps Task from Repo to Local.
     *
     * @param repoTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toLocal(repoTask: RepoTask): LocalTask =
        LocalTask(
            id = repoTask.id,
            completed = repoTask.completed,
            title = repoTask.title,
            description = repoTask.description,
            categoryId = repoTask.categoryId,
            dueDate = repoTask.dueDate,
            creationDate = repoTask.creationDate,
            completedDate = repoTask.completedDate,
            isRepeating = repoTask.isRepeating,
            alarmInterval = repoTask.alarmInterval?.let { alarmIntervalMapper.toLocal(it) }
        )

    /**
     * Maps Task from Local to Repo.
     *
     * @param localTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toRepo(localTask: LocalTask): RepoTask =
        RepoTask(
            id = localTask.id,
            completed = localTask.completed,
            title = localTask.title,
            description = localTask.description,
            categoryId = localTask.categoryId,
            dueDate = localTask.dueDate,
            creationDate = localTask.creationDate,
            completedDate = localTask.completedDate,
            isRepeating = localTask.isRepeating,
            alarmInterval = localTask.alarmInterval?.let { alarmIntervalMapper.toRepo(it) }
        )
}
