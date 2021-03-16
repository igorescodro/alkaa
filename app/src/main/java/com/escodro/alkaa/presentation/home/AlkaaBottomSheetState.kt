package com.escodro.alkaa.presentation.home

import android.os.Parcelable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.escodro.categoryapi.model.Category
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterialApi::class)
@Stable
internal class AlkaaBottomSheetState(
    modalState: ModalBottomSheetState,
    contentState: SheetContentState
) {
    var modalState by mutableStateOf(modalState)
    var contentState by mutableStateOf(contentState)

    companion object {

        @Suppress("FunctionNaming")
        fun Saver(modalState: ModalBottomSheetState): Saver<AlkaaBottomSheetState, *> = Saver(
            save = { it.contentState },
            restore = { AlkaaBottomSheetState(modalState = modalState, contentState = it) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun rememberBottomSheetState(
    modalState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    contentState: SheetContentState = SheetContentState.Empty
): AlkaaBottomSheetState =
    rememberSaveable(saver = AlkaaBottomSheetState.Saver(modalState)) {
        AlkaaBottomSheetState(modalState = modalState, contentState = contentState)
    }

internal sealed class SheetContentState {

    @Parcelize
    object Empty : SheetContentState(), Parcelable

    @Parcelize
    object TaskListSheet : SheetContentState(), Parcelable

    @Parcelize
    data class CategorySheet(val category: Category) : SheetContentState(), Parcelable
}
