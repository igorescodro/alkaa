package com.escodro.local.model

import androidx.room.Embedded
import androidx.room.Entity

/**
 * [Entity] to represent a wrapper of [Task] and [Category].
 *
 * @property task the associated task
 * @property category the associated category
 */
data class TaskWithCategory(
    @Embedded val task: Task,
    @Embedded val category: Category? = null
)
