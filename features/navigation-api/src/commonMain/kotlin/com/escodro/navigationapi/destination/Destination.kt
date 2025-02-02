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
val TopLevelDestinations: List<TopLevel> = listOf(
    HomeDestination.TaskList,
    HomeDestination.Search,
    HomeDestination.CategoryList,
    HomeDestination.Preferences,
)

/**
 * All destinations that should show the top app bar. This is a list because there are no function
 * to get all the sealed classes by a type in Kotlin Multiplatform.
 */
val TopAppBarVisibleDestinations: List<TopAppBarVisible> = listOf(
    HomeDestination.TaskList,
    HomeDestination.Search,
    HomeDestination.CategoryList,
    HomeDestination.Preferences,
    TasksDestination.AddTaskBottomSheet,
    CategoryDestination.CategoryBottomSheet(categoryId = 0),
)
