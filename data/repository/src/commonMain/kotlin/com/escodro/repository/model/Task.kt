package com.escodro.repository.model

import kotlinx.datetime.LocalDateTime

/**
 * Data class to represent a Task.
 *
 * @property id unique task id
 * @property isCompleted indicates if the task is completed
 * @property title the task title
 * @property description the task description
 * @property categoryId the associated category id
 * @property dueDate the due date to the task be notified
 * @property creationDate the date of creation of the task
 * @property completedDate the date of completion of the task
 * @property isRepeating indicates if the task is repeating
 * @property alarmInterval the interval between the repeating
 */
data class Task(
    val id: Long = 0,
    val isCompleted: Boolean = false,
    val title: String,
    val description: String?,
    val categoryId: Long? = null,
    val dueDate: LocalDateTime? = null,
    val creationDate: LocalDateTime? = null,
    val completedDate: LocalDateTime? = null,
    val isRepeating: Boolean = false,
    val alarmInterval: AlarmInterval? = null,
)
