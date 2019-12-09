package com.escodro.repository.model

/**
 * Data class to represent a Task with Category.
 */
data class TaskWithCategory(
    val task: Task,
    val category: Category? = null
)
