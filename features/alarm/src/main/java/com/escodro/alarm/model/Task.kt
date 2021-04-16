package com.escodro.alarm.model

import java.util.Calendar

/**
 * Data class to represent a View Task.
 *
 * @param id unique task id
 * @param title the task title
 * @param dueDate the due date to the task be notified
 * @param isCompleted indicates if the task is completed
 */
data class Task(
    val id: Long,
    val title: String,
    val dueDate: Calendar?,
    val isCompleted: Boolean
)
