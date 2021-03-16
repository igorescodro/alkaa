package com.escodro.category.presentation

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.escodro.categoryapi.model.Category
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
internal class CategoryBottomSheetState(val category: Category) :
    Parcelable {

    @IgnoredOnParcel
    var name by mutableStateOf(category.name)

    @IgnoredOnParcel
    var color by mutableStateOf(category.color)
}
