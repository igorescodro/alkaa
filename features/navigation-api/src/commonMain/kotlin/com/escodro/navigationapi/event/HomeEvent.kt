package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.serialization.Serializable

object HomeEvent {

    @Serializable
    data class OnTabClick(val topLevel: TopLevel) : Event {
        override fun nextDestination(): Destination = topLevel as Destination
    }
}
