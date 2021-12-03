package com.escodro.repository.model

import java.util.Calendar

/**
 * Data class to represent a Task.
 *
 * @property id unique task id
 * @property completed indicates if the task is completed
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
    val completed: Boolean = false,
    val title: String,
    val description: String?,
    val categoryId: Long? = null,
    val dueDate: Calendar? = null,
    val creationDate: Calendar? = null,
    val completedDate: Calendar? = null,
    val isRepeating: Boolean = false,
    val alarmInterval: AlarmInterval? = null
)
