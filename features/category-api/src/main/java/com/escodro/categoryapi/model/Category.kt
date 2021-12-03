package com.escodro.categoryapi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    val color: Int
) : Parcelable
