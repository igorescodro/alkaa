package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.CategoryDestination
import com.escodro.navigationapi.destination.Destination

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
