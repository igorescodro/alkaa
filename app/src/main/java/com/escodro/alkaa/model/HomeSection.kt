package com.escodro.alkaa.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.alkaa.R

/**
 * Enum to represent the sections available in the bottom app bar.
 *
 * @property title title to be shown in top app bar.
 * @property icon icon to be shown in the bottom app bar
 */
enum class HomeSection(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    Tasks(R.string.home_title_tasks, Icons.Outlined.Check),
    Search(R.string.home_title_search, Icons.Outlined.Search),
    Categories(R.string.home_title_categories, Icons.Outlined.Bookmark),
    Settings(R.string.home_title_settings, Icons.Outlined.MoreHoriz)
}
