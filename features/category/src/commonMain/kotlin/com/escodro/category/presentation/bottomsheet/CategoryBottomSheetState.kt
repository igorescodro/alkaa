package com.escodro.category.presentation.bottomsheet

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.escodro.categoryapi.model.Category
import dev.icerock.moko.parcelize.IgnoredOnParcel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Stable
@Parcelize
internal class CategoryBottomSheetState(
    private val category: Category,
) : Parcelable {

    @IgnoredOnParcel
    var id by mutableStateOf(category.id)

    @IgnoredOnParcel
    var name by mutableStateOf(category.name)

    @IgnoredOnParcel
    var color by mutableStateOf(category.color)

    fun isEditing(): Boolean =
        id > 0L

    fun toCategory(): Category =
        Category(
            id = id,
            name = name,
            color = color,
        )
}
