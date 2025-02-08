package com.escodro.navigationapi.marker

import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.parcelable.CommonParcelable
import org.jetbrains.compose.resources.StringResource

/**
 * Marker interface to indicate that the destination is a top-level destination, containing its own
 * stack.
 */
interface TopLevel : TopAppBarVisible, CommonParcelable {

    /**
     * Title of the destination to be used in the top app bar and bottom bar/navigation rail.
     */
    val title: StringResource

    /**
     * Icon of the destination to be used in the top app bar and bottom bar/navigation rail.
     */
    val icon: ImageVector
}
