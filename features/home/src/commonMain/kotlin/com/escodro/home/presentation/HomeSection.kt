package com.escodro.home.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.resources.Res
import com.escodro.resources.home_title_categories
import com.escodro.resources.home_title_search
import com.escodro.resources.home_title_settings
import com.escodro.resources.home_title_tasks
import org.jetbrains.compose.resources.StringResource

/**
 * Enum to represent the sections available in the bottom app bar.
 *
 * @property title title to be shown in top app bar.
 * @property icon icon to be shown in the bottom app bar
 */
enum class HomeSection(
    val title: StringResource,
    val icon: ImageVector,
) {
    Tasks(Res.string.home_title_tasks, Icons.Outlined.Check),
    Search(Res.string.home_title_search, Icons.Outlined.Search),
    Categories(Res.string.home_title_categories, Icons.Outlined.Bookmark),
    Settings(Res.string.home_title_settings, Icons.Outlined.MoreHoriz),
}
