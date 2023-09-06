package com.escodro.categoryapi.model

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

/**
 * Data class to represent a View Category.
 *
 * @property id category id
 * @property name category name
 * @property color category color
 */
@Parcelize
data class Category(
    val id: Long = 0,
    val name: String,
    val color: Int,
) : Parcelable
