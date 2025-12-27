package com.escodro.navigationapi.destination

import com.escodro.navigationapi.marker.TopAppBarVisible
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Back : Destination

    companion object {

        const val URI: String = "app://com.escodro.alkaa"
    }
}

/**
 * All top-level destinations.
 */
val TopLevelDestinations: Set<TopLevel> = setOf(
    HomeDestination.TaskList,
    HomeDestination.Search,
    HomeDestination.CategoryList,
    HomeDestination.Preferences,
)

/**
 * All destinations that should show the top app bar.
 */
val TopAppBarVisibleDestinations: Set<TopAppBarVisible> = setOf(
    HomeDestination.TaskList,
    HomeDestination.Search,
    HomeDestination.CategoryList,
    HomeDestination.Preferences,
    TasksDestination.AddTaskBottomSheet,
    CategoryDestination.CategoryBottomSheet(categoryId = 0),
)
