package com.escodro.navigationapi.destination

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.escodro.navigationapi.marker.TopLevel
import com.escodro.parcelable.CommonIgnoredOnParcel
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

        @CommonIgnoredOnParcel
        override val title: StringResource = Res.string.home_title_tasks

        @CommonIgnoredOnParcel
        override val icon: ImageVector = Icons.Outlined.Check
    }

    @Serializable
    @CommonParcelize
    data object Search : Destination, TopLevel {

        @CommonIgnoredOnParcel
        override val title: StringResource = Res.string.home_title_search

        @CommonIgnoredOnParcel
        override val icon: ImageVector = Icons.Outlined.Search
    }

    @Serializable
    @CommonParcelize
    data object CategoryList : Destination, TopLevel {

        @CommonIgnoredOnParcel
        override val title: StringResource = Res.string.home_title_categories

        @CommonIgnoredOnParcel
        override val icon: ImageVector = Icons.Outlined.Bookmark
    }

    @Serializable
    @CommonParcelize
    data object Preferences : Destination, TopLevel {

        @CommonIgnoredOnParcel
        override val title: StringResource = Res.string.home_title_settings

        @CommonIgnoredOnParcel
        override val icon: ImageVector = Icons.Outlined.MoreHoriz
    }
}
