package com.escodro.home.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.resources.MR
import dev.icerock.moko.resources.StringResource

/**
 * Enum to represent the sections available in the bottom app bar.
 *
 * @property title title to be shown in top app bar.
 * @property icon icon to be shown in the bottom app bar
 */
internal enum class HomeSection(
    val title: StringResource,
    val icon: ImageVector,
) {
    Tasks(MR.strings.home_title_tasks, Icons.Outlined.Check),
    Search(MR.strings.home_title_search, Icons.Outlined.Search),
    Categories(MR.strings.home_title_categories, Icons.Outlined.Bookmark),
    Settings(MR.strings.home_title_settings, Icons.Outlined.MoreHoriz),
}
