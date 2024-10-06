package com.escodro.navigation.destination

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Back : Destination
}
