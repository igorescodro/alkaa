package com.escodro.search.model

import androidx.compose.ui.graphics.Color

/**
 * UI representation of Task results.
 */
internal data class TaskSearchItem(
    val id: Long = 0,
    val completed: Boolean,
    val title: String,
    val categoryColor: Color?,
    val isRepeating: Boolean
)
