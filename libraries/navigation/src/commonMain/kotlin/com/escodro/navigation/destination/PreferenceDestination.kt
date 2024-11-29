package com.escodro.navigation.destination

import kotlinx.serialization.Serializable

object PreferenceDestination {

    @Serializable
    data object About : Destination

    @Serializable
    data object Licenses : Destination

    @Serializable
    data object Tracker : Destination
}
