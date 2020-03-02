package com.escodro.search.model

/**
 * UI representation of Task results.
 */
internal data class TaskSearch(
    val id: Long = 0,
    val completed: Boolean,
    val title: String,
    val categoryColor: String?,
    val isRepeating: Boolean
)
