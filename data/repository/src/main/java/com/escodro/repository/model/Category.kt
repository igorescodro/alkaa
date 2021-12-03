package com.escodro.repository.model

/**
 * Data class to represent a Category.
 *
 * @property id category id
 * @property name category name
 * @property color category color
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val color: String
)
