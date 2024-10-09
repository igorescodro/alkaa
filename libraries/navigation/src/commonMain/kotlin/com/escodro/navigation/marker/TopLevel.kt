package com.escodro.navigation.marker

import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.parcelable.CommonParcelable
import org.jetbrains.compose.resources.StringResource

interface TopLevel : CommonParcelable {

    val title: StringResource

    val icon: ImageVector
}
