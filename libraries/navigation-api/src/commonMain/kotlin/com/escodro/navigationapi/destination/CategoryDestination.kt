package com.escodro.navigationapi.destination

import com.escodro.navigationapi.marker.TopAppBarVisible
import kotlinx.serialization.Serializable

object CategoryDestination {

    @Serializable
    data class CategoryBottomSheet(val categoryId: Long?) : Destination, TopAppBarVisible
}
