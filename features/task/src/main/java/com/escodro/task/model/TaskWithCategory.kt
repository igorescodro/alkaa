package com.escodro.task.model

import com.escodro.categoryapi.model.Category

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
