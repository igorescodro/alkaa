package com.escodro.domain.model

/**
 * Data class to represent a Task with Category.
 *
 * @property task the associated task
 * @property category the associated category
 */
data class TaskWithCategory(
    val task: Task,
    val category: Category? = null
)
