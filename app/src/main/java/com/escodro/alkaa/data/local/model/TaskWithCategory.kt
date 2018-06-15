package com.escodro.alkaa.data.local.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity

/**
 * [Entity] to represent a wrapper of [Task] and [Category].
 */
data class TaskWithCategory(

    @Embedded val task: Task,

    @Embedded val category: Category? = null
)
