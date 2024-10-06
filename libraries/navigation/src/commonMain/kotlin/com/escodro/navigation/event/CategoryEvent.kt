package com.escodro.navigation.event

import com.escodro.navigation.destination.CategoryDestination
import com.escodro.navigation.destination.Destination

object CategoryEvent {

    data object OnNewCategoryClick : Event {
        override fun nextDestination(): Destination =
            CategoryDestination.CategoryBottomSheet(categoryId = null)
    }

    data class OnCategoryClick(val categoryId: Long?) : Event {
        override fun nextDestination(): Destination =
            CategoryDestination.CategoryBottomSheet(categoryId = categoryId)
    }
}
