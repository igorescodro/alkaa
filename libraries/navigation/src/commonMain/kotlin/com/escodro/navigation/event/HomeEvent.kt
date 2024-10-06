package com.escodro.navigation.event

import com.escodro.navigation.destination.Destination
import com.escodro.navigation.destination.HomeDestination
import com.escodro.navigation.marker.TopLevel
import kotlinx.serialization.Serializable

object HomeEvent {

    @Serializable
    data class OnTabClick(val topLevel: TopLevel) : Event {
        override fun nextDestination(): Destination =
            when (topLevel) {
                is HomeDestination.TaskList -> HomeDestination.TaskList
                is HomeDestination.Search -> HomeDestination.Search
                is HomeDestination.CategoryList -> HomeDestination.CategoryList
                is HomeDestination.Preferences -> HomeDestination.Preferences
                else -> throw IllegalArgumentException("Invalid TopLevel")
            }
    }
}
