package com.escodro.navigation.destination

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Back : Destination

    companion object {

        const val URI: String = "app://com.escodro.alkaa"
    }
}
