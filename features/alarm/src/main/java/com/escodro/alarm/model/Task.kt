package com.escodro.alarm.model

import java.util.Calendar

/**
 * Data class to represent a View Task.
 */
data class Task(
    val id: Long,
    val title: String,
    val dueDate: Calendar?,
    val isCompleted: Boolean
)
