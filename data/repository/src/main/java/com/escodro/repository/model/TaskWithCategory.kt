package com.escodro.repository.model

/**
 * Data class to represent a Task with Category.
 *
 * @param task the associated task
 * @param category the associated category
 */
data class TaskWithCategory(
    val task: Task,
    val category: Category? = null
)
