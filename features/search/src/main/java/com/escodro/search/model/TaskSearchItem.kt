package com.escodro.search.model

/**
 * UI representation of Task results.
 */
internal data class TaskSearchItem(
    val id: Long = 0,
    val completed: Boolean,
    val title: String,
    val categoryColor: Int?,
    val isRepeating: Boolean
)
