package com.escodro.category.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.escodro.categoryapi.model.Category

@Stable
internal class CategoryBottomSheetState(val category: Category) {

    private val id by mutableStateOf(category.id)

    var name by mutableStateOf(category.name)

    var color by mutableStateOf(category.color)

    fun isEditing(): Boolean =
        id > 0L

    fun toCategory(): Category =
        Category(
            id = id,
            name = name,
            color = color
        )
}
