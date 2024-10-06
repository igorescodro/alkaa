package com.escodro.navigation.destination

import kotlinx.serialization.Serializable

object CategoryDestination {

    @Serializable
    data class CategoryBottomSheet(val categoryId: Long?) : Destination
}
