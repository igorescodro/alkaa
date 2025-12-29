package com.escodro.local.mapper

import com.escodro.local.Task as LocalTask
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
            task_id = repoTask.id,
            task_is_completed = repoTask.isCompleted,
            task_title = repoTask.title,
            task_description = repoTask.description,
            task_category_id = repoTask.categoryId,
            task_due_date = repoTask.dueDate,
            task_creation_date = repoTask.creationDate,
            task_completed_date = repoTask.completedDate,
            task_is_repeating = repoTask.isRepeating,
            task_alarm_interval = repoTask.alarmInterval?.let { alarmIntervalMapper.toLocal(it) },
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
            id = localTask.task_id,
            isCompleted = localTask.task_is_completed,
            title = localTask.task_title,
            description = localTask.task_description,
            categoryId = localTask.task_category_id,
            dueDate = localTask.task_due_date,
            creationDate = localTask.task_creation_date,
            completedDate = localTask.task_completed_date,
            isRepeating = localTask.task_is_repeating,
            alarmInterval = localTask.task_alarm_interval?.let { alarmIntervalMapper.toRepo(it) },
        )
}
