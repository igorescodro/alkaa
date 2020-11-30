package com.escodro.task.model

import androidx.compose.ui.graphics.Color

/**
 * Data class to represent a View Category.
 *
 * @param id category id
 * @param name category name
 * @param color category color
 */
data class Category(
    val id: Long = 0,
    val name: String?,
    val color: Color
)
