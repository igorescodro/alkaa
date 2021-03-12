package com.escodro.repository.model

/**
 * Data class to represent a Category.
 *
 * @param id category id
 * @param name category name
 * @param color category color
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val color: String
)
