package com.escodro.domain.model

import java.util.Calendar

/**
 * Data class to represent a Task.
 */
data class Task(
    val id: Long = 0,
    val completed: Boolean = false,
    val title: String,
    val description: String?,
    val categoryId: Long? = null,
    val dueDate: Calendar? = null,
    val creationDate: Calendar? = null,
    val completedDate: Calendar? = null
)
