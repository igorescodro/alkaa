package com.escodro.alarm.model

import kotlinx.datetime.LocalDateTime

/**
 * Data class to represent a View Task.
 *
 * @property id unique task id
 * @property title the task title
 * @property dueDate the due date to the task be notified
 * @property isCompleted indicates if the task is completed
 */
data class Task(
    val id: Long,
    val title: String,
    val dueDate: LocalDateTime?,
    val isCompleted: Boolean,
)
