package com.escodro.categoryapi.model

import com.escodro.parcelable.CommonParcelable
import com.escodro.parcelable.CommonParcelize

/**
 * Data class to represent a View Category.
 *
 * @property id category id
 * @property name category name
 * @property color category color
 * @property isEditing flag to indicate if the category is being edited
 */
@CommonParcelize
data class Category(
    val id: Long = 0,
    val name: String,
    val color: Int,
    val isEditing: Boolean = id > 0L,
) : CommonParcelable
