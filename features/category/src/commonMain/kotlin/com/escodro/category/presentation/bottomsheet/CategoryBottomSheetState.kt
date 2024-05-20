package com.escodro.category.presentation.bottomsheet

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.escodro.categoryapi.model.Category
import com.escodro.parcelable.CommonParcelable
import com.escodro.parcelable.CommonParcelize

@Stable
@CommonParcelize
internal class CategoryBottomSheetState(
    private val category: Category,
) : CommonParcelable {

    private var id by mutableStateOf(category.id)
    
    var name by mutableStateOf(category.name)

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
