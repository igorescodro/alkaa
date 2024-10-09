package com.escodro.navigation.destination

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.navigation.marker.TopLevel
import com.escodro.parcelable.CommonParcelize
import com.escodro.resources.Res
import com.escodro.resources.home_title_categories
import com.escodro.resources.home_title_search
import com.escodro.resources.home_title_settings
import com.escodro.resources.home_title_tasks
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

object HomeDestination {

    @Serializable
    @CommonParcelize
    data object TaskList : Destination, TopLevel {
        override val title: StringResource = Res.string.home_title_tasks
        override val icon: ImageVector = Icons.Outlined.Check
    }

    @Serializable
    @CommonParcelize
    data object Search : Destination, TopLevel {
        override val title: StringResource = Res.string.home_title_search
        override val icon: ImageVector = Icons.Outlined.Search
    }

    @Serializable
    @CommonParcelize
    data object CategoryList : Destination, TopLevel {
        override val title: StringResource = Res.string.home_title_categories
        override val icon: ImageVector = Icons.Outlined.Bookmark
    }

    @Serializable
    @CommonParcelize
    data object Preferences : Destination, TopLevel {
        override val title: StringResource = Res.string.home_title_settings
        override val icon: ImageVector = Icons.Outlined.MoreHoriz
    }
}

val topLevelDestinations: List<TopLevel>
    get() = listOf(
        HomeDestination.TaskList,
        HomeDestination.Search,
        HomeDestination.CategoryList,
        HomeDestination.Preferences,
    )
