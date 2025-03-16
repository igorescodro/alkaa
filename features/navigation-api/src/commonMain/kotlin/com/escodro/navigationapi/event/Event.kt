package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination

sealed interface Event {

    fun nextDestination(): Destination

    data object OnBack : Event {
        override fun nextDestination(): Destination = Destination.Back
    }
}
