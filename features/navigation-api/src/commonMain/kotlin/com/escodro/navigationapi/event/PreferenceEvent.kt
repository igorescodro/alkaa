package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.PreferenceDestination

object PreferenceEvent {

    data object OnAboutClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.About
    }

    data object OnLicensesClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.Licenses
    }

    data object OnTrackerClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.Tracker
    }
}
