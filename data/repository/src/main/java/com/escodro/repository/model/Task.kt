package com.escodro.repository.model

import java.util.Calendar

/**
 * Data class to represent a Task.
 */
data class Task(
    val id: Long = 0,
    val completed: Boolean = false,
    val title: String,
    val description: String,
    val dueDate: Calendar? = null,
    val creationDate: Calendar? = null,
    val completedDate: Calendar? = null
)
