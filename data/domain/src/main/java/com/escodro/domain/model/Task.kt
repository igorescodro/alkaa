package com.escodro.domain.model

import java.util.Calendar

/**
 * Data class to represent a Task.
 *
 * @param id unique task id
 * @param completed indicates if the task is completed
 * @param title the task title
 * @param description the task description
 * @param categoryId the associated category id
 * @param dueDate the due date to the task be notified
 * @param creationDate the date of creation of the task
 * @param completedDate the date of completion of the task
 * @param isRepeating indicates if the task is repeating
 * @param alarmInterval the interval between the repeating
 */
data class Task(
    val id: Long = 0,
    val completed: Boolean = false,
    val title: String,
    val description: String? = null,
    val categoryId: Long? = null,
    val dueDate: Calendar? = null,
    val creationDate: Calendar? = null,
    val completedDate: Calendar? = null,
    val isRepeating: Boolean = false,
    val alarmInterval: AlarmInterval? = null
)
