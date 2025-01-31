package com.escodro.navigationapi.destination

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Back : Destination

    companion object {

        const val URI: String = "app://com.escodro.alkaa"
    }
}
