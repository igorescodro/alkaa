package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination

sealed interface Event {

    fun nextDestination(): Destination

    data object OnBack : Event {
        override fun nextDestination(): Destination = Destination.Back
    }

    data object OnTaskListClick : Event {
        override fun nextDestination(): Destination = HomeDestination.TaskList
    }

    data object OnSearchClick : Event {
        override fun nextDestination(): Destination = HomeDestination.Search
    }

    data object OnCategoryListClick : Event {
        override fun nextDestination(): Destination = HomeDestination.CategoryList
    }

    data object OnPreferencesClick : Event {
        override fun nextDestination(): Destination = HomeDestination.Preferences
    }
}
