package com.escodro.local.model

import com.escodro.local.Category
import com.escodro.local.Task

/**
 * Custom entity to represent a wrapper of [Task] and [Category].
 *
 * @property task the associated task
 * @property category the associated category
 */
data class TaskWithCategory(
    val task: Task,
    val category: Category? = null,
)
