package com.escodro.local.model

import androidx.room.Embedded
import androidx.room.Entity
import com.escodro.model.Category
import com.escodro.model.Task

/**
 * [Entity] to represent a wrapper of [Task] and [Category].
 */
data class TaskWithCategory(
    @Embedded val task: Task,
    @Embedded val category: Category? = null
)
